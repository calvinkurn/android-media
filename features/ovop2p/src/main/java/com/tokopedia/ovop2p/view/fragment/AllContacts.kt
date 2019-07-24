package com.tokopedia.ovop2p.view.fragment

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.ovop2p.Constants
import com.tokopedia.ovop2p.OvoP2pRouter
import com.tokopedia.ovop2p.R
import com.tokopedia.ovop2p.di.OvoP2pTransferComponent
import com.tokopedia.ovop2p.view.adapters.AllContactsListCursorAdapter
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

class AllContacts : BaseDaggerFragment(), View.OnClickListener, CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    lateinit var contactsListView: ListView
    lateinit var scanQRImgvw: ImageView
    lateinit var scanQrHeader: TextView

    override fun getScreenName(): String {
        return Constants.ScreenName.FRAGMENT_ALL_CONTACTS
    }

    override fun initInjector() {
        getComponent<OvoP2pTransferComponent>(OvoP2pTransferComponent::class.java).inject(this)
    }

    companion object {
        fun newInstance(): AllContacts {
            return AllContacts()
        }

        fun newInstance(bundle: Bundle): AllContacts {
            val allContactsFragment = newInstance()
            allContactsFragment.setArguments(bundle)
            return allContactsFragment
        }
    }

    override fun onClick(v: View?) {
        var id: Int = v?.id ?: -1
        if (id != -1) {
            when (id) {
                R.id.scanqr_imgvw -> {
                    startActivityForResult((context?.applicationContext as OvoP2pRouter)
                            .gotoQrScannerPage(true), Constants.Keys.CODE_QR_SCANNER_ACTIVITY)
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View = inflater.inflate(R.layout.allcontacts_activity_layout, container, false)
        contactsListView = view.findViewById(R.id.contact_list)
        initContactListAdapter()
        setItemOnClickListener()
        scanQRImgvw = view.findViewById(R.id.scanqr_imgvw)
        scanQRImgvw.setOnClickListener(this)
        scanQrHeader = view.findViewById(R.id.scan_qr_header)
        scanQrHeader.setOnClickListener(this)
        return view
    }

    fun initContactListAdapter() {
        launchCatchError(block = {
            val contacts = createAllContactsCursor()
            val cursorAdapter = activity?.let { AllContactsListCursorAdapter(it, contacts!!, false) }
            contactsListView.adapter = cursorAdapter
        }, onError = {
            activity?.finish()
        })

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            (activity as BaseSimpleActivity).updateTitle(Constants.Headers.LOOK_FOR_NAME_PHONE)
        }
    }


    private suspend fun createAllContactsCursor(): Cursor? = withContext(Dispatchers.IO) {
        activity?.contentResolver?.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                null, null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC ")
    }

    private fun setItemOnClickListener() {
        contactsListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val nameTxtv = view.findViewById<TextView>(R.id.user_name)
            val numberTxtv = view.findViewById<TextView>(R.id.user_num)
            sendResultBack(nameTxtv.text.toString(), numberTxtv.text.toString())
        }
    }

    private fun sendResultBack(usrName: String, usrNum: String) {
        val returnIntent = Intent()
        returnIntent.putExtra(Constants.Keys.USER_NAME, usrName)
        returnIntent.putExtra(Constants.Keys.USER_NUMBER, usrNum)
        activity?.setResult(Constants.Keys.RESULT_CODE_CONTACTS_SELECTION, returnIntent)
        activity?.finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.Keys.CODE_QR_SCANNER_ACTIVITY) {
            var qrResponse: String = Uri.decode(data?.extras?.getString(Constants.Keys.QR_RESPONSE) ?: "")
            sendResultBack("", qrResponse)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!::contactsListView.isInitialized)
        contactsListView.adapter?.let {
            (it as AllContactsListCursorAdapter).cursor?.apply {
                close()
            }
        }
    }

}
