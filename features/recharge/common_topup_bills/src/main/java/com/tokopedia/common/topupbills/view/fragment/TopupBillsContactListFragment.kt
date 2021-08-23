package com.tokopedia.common.topupbills.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.databinding.FragmentContactListBinding
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.adapter.TopupBillsContactListAdapter
import com.tokopedia.common.topupbills.view.model.TopupBillsSavedNumber
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsSavedNumberViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.permission.PermissionCheckerHelper
import javax.inject.Inject


class TopupBillsContactListFragment:
    BaseDaggerFragment(),
    TopupBillsContactListAdapter.OnContactNumberClickListener
{
    private var contacts: MutableList<Contact> = mutableListOf()
    private val contactsProjection: Array<out String> = arrayOf(
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME_PRIMARY,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone.PHOTO_THUMBNAIL_URI
    )

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelFragmentProvider by lazy { ViewModelProvider(requireActivity(), viewModelFactory) }
    private val savedNumberViewModel by lazy {
        viewModelFragmentProvider.get(TopupBillsSavedNumberViewModel::class.java) }

    private lateinit var contactListAdapter: TopupBillsContactListAdapter
    private var binding: FragmentContactListBinding? = null

    override fun getScreenName(): String {
        return TopupBillsContactListFragment::class.java.simpleName
    }

    override fun initInjector() {
        getComponent(CommonTopupBillsComponent::class.java).inject(this)
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

        savedNumberViewModel.searchKeyword.observe(viewLifecycleOwner, { filterData(it) })
    }

    private fun initRecyclerView() {
        contactListAdapter = TopupBillsContactListAdapter(listOf(), this)
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
        this.contacts = contacts
        contactListAdapter.setContacts(this.contacts)

        binding?.commonTopupbillsFavoriteNumberClue?.run {
            if (this@TopupBillsContactListFragment.contacts.isEmpty()){
                hide()
            } else {
                show()
            }
        }
    }

    override fun onContactNumberClick(name: String, number: String) {
        navigateToPDP(name, number)
    }

    private fun navigateToPDP(
        clientName: String,
        clientNumber: String
    ) {
        activity?.run {
            val intent = Intent()
            val searchedNumber = TopupBillsSavedNumber(
                clientName = clientName,
                clientNumber = clientNumber,
                inputNumberActionTypeIndex =
                    TopupBillsSearchNumberFragment.InputNumberActionType.CONTACT.ordinal
            )
            intent.putExtra(TopupBillsSearchNumberActivity.EXTRA_CALLBACK_CLIENT_NUMBER, searchedNumber)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    fun filterData(query: String) {
        val searchClientNumbers = ArrayList<Contact>()

        searchClientNumbers.addAll(contacts.filter {
            it.name.contains(query, true) || it.phoneNumber.contains(query, true)
        })

        contactListAdapter.setContacts(searchClientNumbers)
        binding?.commonTopupbillsFavoriteNumberClue?.run {
            if (searchClientNumbers.isEmpty()) hide() else show()
        }
    }

    inner class Contact(
        val name: String,
        val phoneNumber: String
    )

    companion object {
        fun newInstance(): Fragment {
            return TopupBillsContactListFragment()
        }
        private const val MOD_ASCENDING = " ASC"
    }
}