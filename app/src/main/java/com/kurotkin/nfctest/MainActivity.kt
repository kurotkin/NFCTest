package com.kurotkin.nfctest

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.EditText
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    private var mNfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        toast(NFCUtil.retrieveNFCMessage(this.intent))

    }

    override fun onResume() {
        super.onResume()
        mNfcAdapter?.let {
            NFCUtil.enableNFCInForeground(it, this, javaClass)
        }
    }

    override fun onPause() {
        super.onPause()
        mNfcAdapter?.let {
            NFCUtil.disableNFCInForeground(it, this)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
//        val messageWrittenSuccessfully = NFCUtil.createNFCMessage(messageEditText.toString(), intent)
//        toast(messageWrittenSuccessfully.ifElse("Successful Written to Tag", "Something When wrong Try Again"))
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == intent!!.action) {
            Log.d("ACTION_NDEF_DISCOVERED", "Log")
            toast("ACTION_NDEF_DISCOVERED")

            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages = rawMessages.map { it as NdefMessage }

                Log.d(messages[0].toString(), "Log")
                toast("ACTION_NDEF_DISCOVERED")

            }
        }
        if (NfcAdapter.ACTION_TECH_DISCOVERED == intent!!.action) {
//            Log.d("ACTION_TECH_DISCOVERED", "Log")
//            toast("ACTION_TECH_DISCOVERED")

            val tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
            var hexdump = String()
            for (i in tagId) {
                val op :Int = i.toInt() and 0xff
                var x = Integer.toHexString(op)
                if (x.length == 1) {
                    x = "0" + x
                }
                hexdump += x + " "
            }

            toast(hexdump)
            val nametxt = findViewById (R.id.messageEditText) as EditText
            nametxt.setText("$hexdump\n${nametxt.text}")

            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                Log.d(rawMessages.toString(), "Log")
                //toast(rawMessages.toString())

            }
        }
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent!!.action) {
            Log.d("ACTION_TAG_DISCOVERED", "Log")
            toast("ACTION_TAG_DISCOVERED")

            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)?.also { rawMessages ->
                val messages = rawMessages.map { it as NdefMessage }

                Log.d(messages[0].toString(), "Log")

            }
        }
    }


}

fun <T> Boolean.ifElse(primaryResult: T, secondaryResult: T) = if (this) primaryResult else secondaryResult