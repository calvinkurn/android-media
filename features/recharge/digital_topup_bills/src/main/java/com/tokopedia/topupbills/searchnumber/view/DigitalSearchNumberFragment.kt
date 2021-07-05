package com.tokopedia.topupbills.searchnumber.view

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tokopedia.common.topupbills.data.TopupBillsFavNumberItem
import com.tokopedia.common.topupbills.view.fragment.TopupBillsSearchNumberFragment
import com.tokopedia.utils.permission.PermissionCheckerHelper
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.common.analytics.DigitalTopupAnalytics
import com.tokopedia.topupbills.searchnumber.di.DigitalTelcoSearchComponent
import com.tokopedia.topupbills.telco.common.covertContactUriToContactData
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_search_number_telco.*
import java.util.*
import javax.inject.Inject

class DigitalSearchNumberFragment : TopupBillsSearchNumberFragment() {

    @Inject
    lateinit var permissionCheckerHelper: PermissionCheckerHelper

    @Inject
    lateinit var topupAnalytics: DigitalTopupAnalytics


    override fun initInjector() {
        getComponent(DigitalTelcoSearchComponent::class.java).inject(this)
    }

    override fun getScreenName(): String? {
        return DigitalSearchNumberFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_search_number_telco, container, false)
        searchInputNumber = view.findViewById(R.id.telco_search_number_input_view)
        favNumberRecyclerView = view.findViewById(R.id.telco_search_number_rv)
        return view
    }

    override fun initView() {
        super.initView()
        telco_search_number_contact_picker.setOnClickListener {
            inputNumberActionType = InputNumberActionType.CONTACT
            navigateContact()
        }
    }

    private fun navigateContact() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            activity?.let {
                permissionCheckerHelper.checkPermission(it,
                        PermissionCheckerHelper.Companion.PERMISSION_READ_CONTACT,
                        object : PermissionCheckerHelper.PermissionCheckListener {
                            override fun onPermissionDenied(permissionText: String) {
                                permissionCheckerHelper.onPermissionDenied(it, permissionText)
                            }

                            override fun onNeverAskAgain(permissionText: String) {
                                permissionCheckerHelper.onNeverAskAgain(it, permissionText)
                            }

                            override fun onPermissionGranted() {
                                openContactPicker()
                            }
                        })
            }
        } else {
            openContactPicker()
        }
    }

    fun openContactPicker() {
        val contactPickerIntent = Intent(
                Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        try {
            startActivityForResult(contactPickerIntent, REQUEST_CODE_CONTACT_PICKER)
        } catch (e: ActivityNotFoundException) {
            view?.let {
                Toaster.build(it, getString(R.string.error_message_contact_not_found), Toaster.LENGTH_LONG, Toaster.TYPE_NORMAL).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CODE_CONTACT_PICKER) {
                    activity?.let {
                        telco_search_number_input_view.searchBarTextField.setText("")
                        data.data?.run {
                            val contact = this.covertContactUriToContactData(it.contentResolver)
                            telco_search_number_input_view.searchBarTextField.setText(contact.contactNumber)
                        }
                    }
                }
            }
        }
    }

    override fun onSearchDone(text: String) {
        pickOrderClientNumber(text)
    }

    override fun onSearchReset() {
        super.onSearchReset()
        topupAnalytics.eventClearInputNumber()
    }

    companion object {
        const val REQUEST_CODE_CONTACT_PICKER = 75

        fun newInstance(clientNumberType: String, number: String,
                        numberList: List<TopupBillsFavNumberItem>): Fragment {
            val fragment = DigitalSearchNumberFragment()
            val bundle = Bundle()
            bundle.putString(ARG_PARAM_EXTRA_CLIENT_NUMBER, clientNumberType)
            bundle.putString(ARG_PARAM_EXTRA_NUMBER, number)
            if (numberList.isNotEmpty()) {
                bundle.putParcelableArrayList(ARG_PARAM_EXTRA_NUMBER_LIST, numberList as ArrayList<out Parcelable>)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}
