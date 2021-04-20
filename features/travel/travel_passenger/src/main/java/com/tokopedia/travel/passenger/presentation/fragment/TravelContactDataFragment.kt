package com.tokopedia.travel.passenger.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.travel.country_code.presentation.activity.PhoneCodePickerActivity
import com.tokopedia.travel.country_code.presentation.fragment.PhoneCodePickerFragment
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.travel.passenger.R
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel
import com.tokopedia.travel.passenger.data.entity.TravelUpsertContactModel
import com.tokopedia.travel.passenger.di.TravelPassengerComponent
import com.tokopedia.travel.passenger.presentation.activity.TravelContactDataActivity
import com.tokopedia.travel.passenger.presentation.adapter.TravelContactArrayAdapter
import com.tokopedia.travel.passenger.presentation.model.TravelContactData
import com.tokopedia.travel.passenger.presentation.viewmodel.TravelContactDataViewModel
import com.tokopedia.travel.passenger.util.TravelPassengerGqlMutation
import com.tokopedia.travel.passenger.util.TravelPassengerGqlQuery
import kotlinx.android.synthetic.main.fragment_travel_contact_data.*
import javax.inject.Inject


class TravelContactDataFragment : BaseDaggerFragment(), TravelContactArrayAdapter.ContactArrayListener {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var contactViewModel: TravelContactDataViewModel

    lateinit var contactData: TravelContactData
    var selectedContact = TravelContactListModel.Contact()

    lateinit var spinnerAdapter: ArrayAdapter<String>
    private val spinnerData = mutableListOf<String>()

    lateinit var travelContactArrayAdapter: TravelContactArrayAdapter
    var travelProduct: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            contactViewModel = viewModelProvider.get(TravelContactDataViewModel::class.java)
        }

        arguments?.let {
            contactData = it.getParcelable(TravelContactDataActivity.EXTRA_INITIAL_CONTACT_DATA)
                    ?: TravelContactData()
            travelProduct = it.getString(TravelContactDataActivity.EXTRA_TRAVEL_PRODUCT) ?: ""
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_travel_contact_data, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        contactViewModel.getContactList(TravelPassengerGqlQuery.CONTACT_LIST, travelProduct)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        contactViewModel.contactListResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer { contactList ->
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
                    contactData.phoneCountry = countryPhoneCode.countryId

                    spinnerData.clear()
                    spinnerData += getString(R.string.phone_code_format, contactData.phoneCode)
                    spinnerAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    fun initView() {
        til_contact_name.setLabel(getString(R.string.travel_contact_data_name_title))

        context?.let {
            travelContactArrayAdapter = TravelContactArrayAdapter(it, R.layout.layout_travel_passenger_autocompletetv, arrayListOf(), this)
            (til_contact_name.editText as AutoCompleteTextView).setAdapter(travelContactArrayAdapter)

            (til_contact_name.editText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ -> autofillView(travelContactArrayAdapter.getItem(position)) }

        }

        til_contact_name.editText.setText(contactData.name)
        til_contact_name.setErrorTextAppearance(R.style.ErrorTextAppearance)

        til_contact_email.textFieldInput.setText(contactData.email)

        til_contact_phone_number.textFieldInput.setText(contactData.phone)

        val initialPhoneCode = getString(R.string.phone_code_format, contactData.phoneCode)
        if (contactData.phoneCode != 0) spinnerData += initialPhoneCode
        else spinnerData += getString(R.string.phone_code_format, DEFAULT_PHONE_CODE_NUMBER_ID)
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

        layout_fragment.setOnTouchListener { _, _ ->
            clearAllKeyboardFocus()
            true
        }
    }

    private fun clearAllKeyboardFocus() {
        KeyboardHandler.hideSoftKeyboard(activity)
    }

    private fun autofillView(contact: TravelContactListModel.Contact?) {
        if (contact != null) {
            selectedContact = TravelContactListModel.Contact(fullName = contact.fullName, email = contact.email, phoneNumber = contact.phoneNumber)

            til_contact_email.textFieldInput.setText(contact.email)
            til_contact_phone_number.textFieldInput.setText(contact.phoneNumber)

            contactData.phoneCode = contact.phoneCountryCode
            spinnerData.clear()
            if (contact.phoneCountryCode != 0) spinnerData += getString(R.string.phone_code_format, contact.phoneCountryCode)
            else spinnerData += getString(R.string.phone_code_format, DEFAULT_PHONE_CODE_NUMBER_ID)
            spinnerAdapter.notifyDataSetChanged()
        }
    }

    private fun onSaveButtonClicked() {
        if (validateData()) {
            contactData.name = til_contact_name.editText.text.toString()
            contactData.email = til_contact_email.textFieldInput.text.toString()
            contactData.phone = til_contact_phone_number.textFieldInput.text.toString()
            contactData.phoneCode = (sp_contact_phone_code.selectedItem as String).toInt()

            contactViewModel.updateContactList(TravelPassengerGqlMutation.UPSERT_CONTACT,
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
        resetEditTextError()
        if (til_contact_name.editText.text.isNullOrBlank()) {
            til_contact_name.error = getString(R.string.travel_contact_data_name_error)
            isValid = false
        } else if (isNotAplhabetOrSpaceOnly(til_contact_name.editText.text.toString())) {
            til_contact_name.error = getString(R.string.travel_contact_data_name_alphabet_only)
            isValid = false
        }
        if (!isValidEmail(til_contact_email.textFieldInput.text.toString())) {
            til_contact_email.setError(true)
            til_contact_email.setMessage(getString(R.string.travel_contact_data_email_error))
            isValid = false
        }
        if (til_contact_phone_number.textFieldInput.text.length < MIN_PHONE_NUMBER_DIGIT) {
            til_contact_phone_number.setError(true)
            til_contact_phone_number.setMessage(getString(R.string.travel_contact_data_phone_number_error))
            isValid = false
        }
        return isValid
    }

    private fun isNotAplhabetOrSpaceOnly(string: String): Boolean {
        return !string.matches(Regex("^[a-zA-Z\\s]*$"))
    }

    private fun resetEditTextError() {
        til_contact_email.setMessage("")
        til_contact_email.setError(false)

        til_contact_name.error = ""
        til_contact_phone_number.setMessage("")
        til_contact_email.setError(false)
    }

    private fun isValidEmail(contactEmail: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches() && !contactEmail.contains(".@") && !contactEmail.contains("@.")
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(TravelPassengerComponent::class.java).inject(this)
    }

    override fun getFilterText(): String {
        return til_contact_name.editText.text.toString()
    }

    companion object {
        const val EXTRA_CONTACT_DATA = "extra_contact_data"
        const val REQUEST_CODE_PHONE_CODE = 300
        const val MIN_PHONE_NUMBER_DIGIT = 9
        const val DEFAULT_PHONE_CODE_NUMBER_ID = 62

        fun getInstance(contactData: TravelContactData, travelProduct: String): TravelContactDataFragment =
                TravelContactDataFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(TravelContactDataActivity.EXTRA_INITIAL_CONTACT_DATA, contactData)
                        putString(TravelContactDataActivity.EXTRA_TRAVEL_PRODUCT, travelProduct)
                    }
                }
    }
}