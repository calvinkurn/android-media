package com.tokopedia.common.topupbills.view.fragment

import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.databinding.FragmentContactListBinding
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.view.adapter.TopupBillsContactListAdapter
import com.tokopedia.utils.permission.PermissionCheckerHelper
import javax.inject.Inject


class TopupBillsContactListFragment: BaseDaggerFragment() {

    private val contactsProjection: Array<out String> = arrayOf(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI
    )

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    private lateinit var contactListAdapter: TopupBillsContactListAdapter
    private var binding: FragmentContactListBinding? = null

    override fun getScreenName(): String {
        return TopupBillsContactListFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CommonTopupBillsComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContactListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        loadData()
    }

    private fun initRecyclerView() {
        contactListAdapter = TopupBillsContactListAdapter(listOf())
        binding?.commonTopupBillsContactsRv?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = contactListAdapter
        }
    }

    private fun loadData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper.checkPermission(
                this,
                PermissionCheckerHelper.Companion.PERMISSION_READ_CONTACT,
                object : PermissionCheckerHelper.PermissionCheckListener {
                    override fun onPermissionDenied(permissionText: String) {
                        context?.let {
                            permissionCheckerHelper.onPermissionDenied(
                                it,
                                permissionText
                            )
                        }
                    }

                    override fun onNeverAskAgain(permissionText: String) {
                        context?.let { permissionCheckerHelper.onNeverAskAgain(it, permissionText) }
                    }

                    override fun onPermissionGranted() {
                        loadContacts()
                    }
                })
        } else {
            //TODO: [Misael] CHECK INII BISA GA
            loadContacts()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        context?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheckerHelper.onRequestPermissionsResult(it,
                    requestCode, permissions,
                    grantResults)
            }
        }
    }

    private fun loadContacts() {
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + MOD_ASCENDING
        val cursor = activity?.contentResolver?.query(uri, contactsProjection, null, null, sort)

        val contacts = mutableListOf<Contact>()

        if (cursor != null && cursor.count > 0) {
            while (cursor.moveToNext()) {
                val name = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.NUMBER))
                contacts.add(Contact(name, phoneNumber))
            }
        }

        cursor?.close()
        contactListAdapter.setContacts(contacts)
    }

    private fun getPhoneNumber(id: String): String? {
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID  + " =?"

        val phoneCursor = activity?.contentResolver?.query(
            uri, null, selection, arrayOf(id), null
        )

        if (phoneCursor != null && phoneCursor.moveToNext()) {
            return phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
        }

        phoneCursor?.close()
        return null
    }

    inner class Contact(
        val name: String,
        val phoneNumber: String
    )

    companion object {
        private const val MOD_ASCENDING = " ASC"
    }
}