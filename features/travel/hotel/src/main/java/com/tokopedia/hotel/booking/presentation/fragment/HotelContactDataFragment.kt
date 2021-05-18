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
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.hotel.booking.di.HotelBookingComponent
import com.tokopedia.hotel.booking.presentation.activity.HotelContactDataActivity
import com.tokopedia.hotel.booking.presentation.viewmodel.HotelBookingViewModel
import com.tokopedia.travel.country_code.presentation.activity.PhoneCodePickerActivity
import com.tokopedia.travel.country_code.presentation.fragment.PhoneCodePickerFragment
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel
import com.tokopedia.travel.passenger.data.entity.TravelUpsertContactModel
import com.tokopedia.travel.passenger.presentation.adapter.TravelContactArrayAdapter
import com.tokopedia.travel.passenger.presentation.model.TravelContactData
import com.tokopedia.travel.passenger.util.TravelPassengerGqlMutation
import com.tokopedia.travel.passenger.util.TravelPassengerGqlQuery
import kotlinx.android.synthetic.main.fragment_hotel_contact_data.*
import javax.inject.Inject

class HotelContactDataFragment : BaseDaggerFragment(), TravelContactArrayAdapter.ContactArrayListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var bookingViewModel: HotelBookingViewModel

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(com.tokopedia.travel.passenger.R.layout.fragment_travel_contact_data, container, false)

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
        til_contact_name.setLabel(getString(com.tokopedia.travel.passenger.R.string.travel_contact_data_name_title))
        til_contact_name.setHint(getString(com.tokopedia.travel.passenger.R.string.travel_contact_data_name_hint))

        context?.let {
            travelContactArrayAdapter = TravelContactArrayAdapter(it, com.tokopedia.travel.passenger.R.layout.layout_travel_passenger_autocompletetv, arrayListOf(), this)
            (til_contact_name.getAutoCompleteTextView() as AutoCompleteTextView).setAdapter(travelContactArrayAdapter)

            (til_contact_name.getAutoCompleteTextView() as AutoCompleteTextView).setOnItemClickListener { parent, view, position, id -> autofillView(travelContactArrayAdapter.getItem(position)) }
        }

        til_contact_name.setEditableText(contactData.name)

        til_contact_email.setLabel(getString(com.tokopedia.travel.passenger.R.string.travel_contact_data_email_title))
        til_contact_email.setHint(getString(com.tokopedia.travel.passenger.R.string.travel_contact_data_email_hint))
        til_contact_email.setEditableText(contactData.email)

        til_contact_phone_number.setInputType(InputType.TYPE_CLASS_NUMBER)
        til_contact_phone_number.setHint(getString(com.tokopedia.travel.passenger.R.string.travel_contact_data_phone_number_hint))
        til_contact_phone_number.setEditableText(contactData.phone)

        val initialPhoneCode = getString(com.tokopedia.common.travel.R.string.phone_code_format, contactData.phoneCode)
        spinnerData += initialPhoneCode
        context?.run {
            spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerData)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        sp_contact_phone_code.adapter = spinnerAdapter
        sp_contact_phone_code.setSelection(0)
        sp_contact_phone_code.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                startActivityForResult(PhoneCodePickerActivity.getCallingIntent(requireContext()), REQUEST_CODE_PHONE_CODE)
            }
            true
        }

        contact_data_button.setOnClickListener { onSaveButtonClicked() }
    }

    private fun autofillView(contact: TravelContactListModel.Contact?) {
        if (contact != null) {
            selectedContact = TravelContactListModel.Contact(fullName = contact.fullName, email = contact.email, phoneNumber = contact.phoneNumber)

            til_contact_email.setEditableText(contact.email)
            til_contact_phone_number.setEditableText(contact.phoneNumber)

            contactData.phoneCode = contact.phoneCountryCode
            spinnerData.clear()
            spinnerData += getString(com.tokopedia.common.travel.R.string.phone_code_format, contact.phoneCountryCode)
            spinnerAdapter.notifyDataSetChanged()
        }
    }

    private fun onSaveButtonClicked() {
        if (validateData()) {
            contactData.name = til_contact_name.getEditableValue()
            contactData.email = til_contact_email.getEditableValue()
            contactData.phone = til_contact_phone_number.getEditableValue()
            contactData.phoneCode = (sp_contact_phone_code.selectedItem as String).toInt()

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
        if (til_contact_name.getEditableValue().isNullOrBlank()) {
            til_contact_name.setError(getString(com.tokopedia.travel.passenger.R.string.travel_contact_data_name_error))
            isValid = false
        }
        if (!isValidEmail(til_contact_email.getEditableValue())) {
            til_contact_email.setError(getString(com.tokopedia.travel.passenger.R.string.travel_contact_data_email_error))
            isValid = false
        }
        if (til_contact_phone_number.getEditableValue().length < MIN_PHONE_NUMBER_DIGIT) {
            til_contact_phone_number.setError(getString(com.tokopedia.travel.passenger.R.string.travel_contact_data_phone_number_error))
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
        return til_contact_name.getEditableValue()
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