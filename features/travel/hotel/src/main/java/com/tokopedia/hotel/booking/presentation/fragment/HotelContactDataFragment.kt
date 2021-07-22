package com.tokopedia.hotel.booking.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.hotel.booking.di.HotelBookingComponent
import com.tokopedia.hotel.booking.presentation.activity.HotelContactDataActivity
import com.tokopedia.hotel.booking.presentation.viewmodel.HotelBookingViewModel
import com.tokopedia.hotel.databinding.FragmentHotelContactDataBinding
import com.tokopedia.travel.country_code.presentation.activity.PhoneCodePickerActivity
import com.tokopedia.travel.country_code.presentation.fragment.PhoneCodePickerFragment
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.travel.passenger.R
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel
import com.tokopedia.travel.passenger.data.entity.TravelUpsertContactModel
import com.tokopedia.travel.passenger.presentation.adapter.TravelContactArrayAdapter
import com.tokopedia.travel.passenger.presentation.model.TravelContactData
import com.tokopedia.travel.passenger.util.TravelPassengerGqlMutation
import com.tokopedia.travel.passenger.util.TravelPassengerGqlQuery
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class HotelContactDataFragment : BaseDaggerFragment(), TravelContactArrayAdapter.ContactArrayListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var bookingViewModel: HotelBookingViewModel

    private var binding by autoClearedNullable<FragmentHotelContactDataBinding>()

    lateinit var contactData: TravelContactData
    var selectedContact = TravelContactListModel.Contact()

    lateinit var spinnerAdapter: ArrayAdapter<String>
    val spinnerData = mutableListOf<String>()

    lateinit var travelContactArrayAdapter: TravelContactArrayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            bookingViewModel = viewModelProvider.get(HotelBookingViewModel::class.java)
        }

        arguments?.let {
            contactData = it.getParcelable(HotelContactDataActivity.EXTRA_INITIAL_CONTACT_DATA)
                    ?: TravelContactData()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHotelContactDataBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        bookingViewModel.getContactList(TravelPassengerGqlQuery.CONTACT_LIST)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        bookingViewModel.contactListResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer { contactList ->
            contactList?.let { travelContactArrayAdapter.updateItem(it.toMutableList()) }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_PHONE_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val countryPhoneCode = data?.getParcelableExtra(PhoneCodePickerFragment.EXTRA_SELECTED_PHONE_CODE)
                            ?: TravelCountryPhoneCode()
                    contactData.phoneCode = countryPhoneCode.countryPhoneCode

                    spinnerData.clear()
                    spinnerData += getString(com.tokopedia.common.travel.R.string.phone_code_format, contactData.phoneCode)
                    spinnerAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    fun initView() {
        binding?.tilContactName?.setLabel(getString(com.tokopedia.travel.passenger.R.string.travel_contact_data_name_title))
        binding?.tilContactName?.setHint(getString(com.tokopedia.travel.passenger.R.string.travel_contact_data_name_hint))

        context?.let {
            travelContactArrayAdapter = TravelContactArrayAdapter(it, com.tokopedia.travel.passenger.R.layout.layout_travel_passenger_autocompletetv, arrayListOf(), this)
            (binding?.tilContactName?.getAutoCompleteTextView() as AutoCompleteTextView).setAdapter(travelContactArrayAdapter)

            (binding?.tilContactName?.getAutoCompleteTextView() as AutoCompleteTextView).setOnItemClickListener { parent, view, position, id -> autofillView(travelContactArrayAdapter.getItem(position)) }
        }

        binding?.tilContactName?.setEditableText(contactData.name)

        binding?.tilContactEmail?.setLabel(getString(com.tokopedia.travel.passenger.R.string.travel_contact_data_email_title))
        binding?.tilContactEmail?.setHint(getString(com.tokopedia.travel.passenger.R.string.travel_contact_data_email_hint))
        binding?.tilContactEmail?.setEditableText(contactData.email)

        binding?.tilContactPhoneNumber?.setInputType(InputType.TYPE_CLASS_NUMBER)
        binding?.tilContactPhoneNumber?.setHint(getString(com.tokopedia.travel.passenger.R.string.travel_contact_data_phone_number_hint))
        binding?.tilContactPhoneNumber?.setEditableText(contactData.phone)

        val initialPhoneCode = getString(com.tokopedia.common.travel.R.string.phone_code_format, contactData.phoneCode)
        spinnerData += initialPhoneCode
        context?.run {
            spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerData)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding?.spContactPhoneCode?.adapter = spinnerAdapter
        binding?.spContactPhoneCode?.setSelection(0)
        binding?.spContactPhoneCode?.setOnTouchListener {v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                v.performClick()
                startActivityForResult(PhoneCodePickerActivity.getCallingIntent(requireContext()), REQUEST_CODE_PHONE_CODE)
            }
            true
        }

        binding?.spContactPhoneCode?.setOnClickListener { onSaveButtonClicked() }
    }

    private fun autofillView(contact: TravelContactListModel.Contact?) {
        if (contact != null) {
            selectedContact = TravelContactListModel.Contact(fullName = contact.fullName, email = contact.email, phoneNumber = contact.phoneNumber)

            binding?.tilContactEmail?.setEditableText(contact.email)
            binding?.tilContactPhoneNumber?.setEditableText(contact.phoneNumber)

            contactData.phoneCode = contact.phoneCountryCode
            spinnerData.clear()
            spinnerData += getString(com.tokopedia.common.travel.R.string.phone_code_format, contact.phoneCountryCode)
            spinnerAdapter.notifyDataSetChanged()
        }
    }

    private fun onSaveButtonClicked() {
        if (validateData()) {
            contactData.name = binding?.tilContactName?.getEditableValue() ?: ""
            contactData.email = binding?.tilContactEmail?.getEditableValue() ?: ""
            contactData.phone = binding?.tilContactPhoneNumber?.getEditableValue() ?: ""
            contactData.phoneCode = (binding?.spContactPhoneCode?.selectedItem as String).toInt()

            bookingViewModel.updateContactList(TravelPassengerGqlMutation.UPSERT_CONTACT,
                    TravelUpsertContactModel.Contact(fullName = contactData.name, email = contactData.email, phoneNumber = contactData.phone,
                            phoneCountryCode = contactData.phoneCode))

            activity?.run {
                val intent = Intent()
                intent.putExtra(EXTRA_CONTACT_DATA, contactData)
                this.setResult(Activity.RESULT_OK, intent)
                this.finish()
            }
        }
    }

    private fun validateData(): Boolean {
        var isValid = true
        if (binding?.tilContactName?.getEditableValue().isNullOrBlank()) {
            binding?.tilContactName?.setError(getString(com.tokopedia.travel.passenger.R.string.travel_contact_data_name_error))
            isValid = false
        }
        if (!isValidEmail(binding?.tilContactEmail?.getEditableValue() ?: "")) {
            binding?.tilContactEmail?.setError(getString(com.tokopedia.travel.passenger.R.string.travel_contact_data_email_error))
            isValid = false
        }
        if (binding?.tilContactPhoneNumber?.getEditableValue()?.length ?: 0 < MIN_PHONE_NUMBER_DIGIT) {
            binding?.tilContactPhoneNumber?.setError(getString(com.tokopedia.travel.passenger.R.string.travel_contact_data_phone_number_error))
            isValid = false
        }
        return isValid
    }

    private fun isValidEmail(contactEmail: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches() && !contactEmail.contains(".@") && !contactEmail.contains("@.")
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(HotelBookingComponent::class.java).inject(this)
    }

    override fun getFilterText(): String {
        return binding?.tilContactName?.getEditableValue() ?: ""
    }

    companion object {
        const val EXTRA_CONTACT_DATA = "extra_contact_data"
        const val REQUEST_CODE_PHONE_CODE = 300
        const val MIN_PHONE_NUMBER_DIGIT = 9

        fun getInstance(contactData: TravelContactData): HotelContactDataFragment =
                HotelContactDataFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(HotelContactDataActivity.EXTRA_INITIAL_CONTACT_DATA, contactData)
                    }
                }
    }
}