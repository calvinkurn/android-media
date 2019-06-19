package com.tokopedia.ovop2p.view.activity

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.view.View
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity

import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.R
import com.tokopedia.ovop2p.view.adapters.AllContactsListCursorAdapter

class AllContactsActivity : BaseSimpleActivity(), View.OnClickListener {
    override fun getNewFragment(): Fragment {
        return Fragment()
    }

    lateinit var contactsListView: ListView
    lateinit var scanQRImgvw: ImageView
    lateinit var scanQrHeader: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.allcontacts_activity_layout)
        contactsListView = findViewById(R.id.contact_list)
        val contacts = createAllContactsCursor()
        val cursorAdapter = AllContactsListCursorAdapter(this, contacts!!, false)
        contactsListView.adapter = cursorAdapter
        setItemOnClickListener()
        scanQRImgvw = findViewById(R.id.scanqr_imgvw)
        scanQRImgvw.setOnClickListener(this)
        scanQrHeader = findViewById(R.id.scan_qr_header)
        scanQrHeader.setOnClickListener(this)
        updateTitle(Constants.Headers.LOOK_FOR_NAME_PHONE)
    }

    private fun createAllContactsCursor(): Cursor? {
        return contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
    }

    private fun setItemOnClickListener() {
        contactsListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val nameTxtv = view.findViewById<TextView>(R.id.user_name)
            val numberTxtv = view.findViewById<TextView>(R.id.user_num)
            val returnIntent = Intent()
            returnIntent.putExtra(Constants.Keys.USER_NAME, nameTxtv.text)
            returnIntent.putExtra(Constants.Keys.USER_NUMBER, numberTxtv.text)
            setResult(Constants.Keys.RESULT_CODE_CONTACTS_SELECTION, returnIntent)
            finish()
        }
    }

    override fun onClick(v: View?) {
        var id: Int = v?.id ?: -1
        if (id != -1) {
            when (id) {
                R.id.scanqr_imgvw or R.id.scan_qr_header -> {
                    //start qr scanner activity for result
                }
            }
        }
    }
}
