package com.tokopedia.common.topupbills.view.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.data.TopupBillsContact
import com.tokopedia.common.topupbills.data.source.ContactDataSource
import com.tokopedia.common.topupbills.databinding.FragmentContactListBinding
import com.tokopedia.common.topupbills.di.CommonTopupBillsComponent
import com.tokopedia.common.topupbills.utils.CommonTopupBillsDataMapper
import com.tokopedia.common.topupbills.view.activity.TopupBillsSearchNumberActivity
import com.tokopedia.common.topupbills.view.adapter.TopupBillsContactListAdapter
import com.tokopedia.common.topupbills.view.model.TopupBillsSavedNumber
import com.tokopedia.common.topupbills.view.typefactory.ContactListTypeFactoryImpl
import com.tokopedia.common.topupbills.view.viewmodel.TopupBillsSavedNumberViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.permission.PermissionCheckerHelper
import javax.inject.Inject


open class TopupBillsContactListFragment:
    BaseDaggerFragment(),
    TopupBillsContactListAdapter.ContactNumberClickListener,
    TopupBillsContactListAdapter.ContactPermissionListener
{
    private var contacts: MutableList<TopupBillsContact> = mutableListOf()

    @Inject
    lateinit var contactDataSource: ContactDataSource

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
        if (permissionCheckerHelper.hasPermission(
                requireContext(),
                arrayOf(PermissionCheckerHelper.Companion.PERMISSION_READ_CONTACT))) {
            loadContacts()
        } else {
            contactListAdapter.setPermissionState()
        }

        savedNumberViewModel.searchKeyword.observe(viewLifecycleOwner, { filterData(it) })

        savedNumberViewModel.refreshSearchBar.observe(viewLifecycleOwner, { position ->
            if (position == TopupBillsSavedNumberFragment.POSITION_CONTACT_LIST) {
                savedNumberViewModel.setClueVisibility(contacts.isNotEmpty())
                savedNumberViewModel.enableSearchBar(contacts.isNotEmpty())
            }
        })
    }

    private fun initRecyclerView() {
        val typeFactory = ContactListTypeFactoryImpl(this, this)
        contactListAdapter = TopupBillsContactListAdapter(listOf(), typeFactory)
        binding?.commonTopupBillsContactsRv?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = contactListAdapter
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

    protected open fun loadContacts() {
        this.contacts = contactDataSource.getContactList()
        if (this.contacts.isEmpty()) {
            contactListAdapter.setNotFoundState()
            savedNumberViewModel.setClueVisibility(false)
            savedNumberViewModel.enableSearchBar(false)
        } else {
            contactListAdapter.setContacts(
                CommonTopupBillsDataMapper.mapContactToDataView(this.contacts))
            savedNumberViewModel.setClueVisibility(true)
            savedNumberViewModel.enableSearchBar(true)
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

    override fun onSettingButtonClick() {
        goToApplicationDetailActivity()
    }

    private fun goToApplicationDetailActivity() {
        activity?.let {
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts("package", it.packageName, null)
            intent.data = uri
            it.startActivity(intent)
        }
    }

    private fun filterData(query: String) {
        val searchClientNumbers = ArrayList<TopupBillsContact>()

        searchClientNumbers.addAll(contacts.filter {
            it.name.contains(query, true) || it.phoneNumber.contains(query, true)
        })

        contactListAdapter.setContacts(
            CommonTopupBillsDataMapper.mapContactToDataView(searchClientNumbers))
        if (searchClientNumbers.isEmpty()) {
            if (contacts.isNotEmpty()) {
                contactListAdapter.setEmptyState()
            }
            savedNumberViewModel.setClueVisibility(false)
        } else {
            savedNumberViewModel.setClueVisibility(true)
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
    }
}