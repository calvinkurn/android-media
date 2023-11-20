package com.tokopedia.travel.passenger.presentation.fragment

import android.annotation.SuppressLint
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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.travel.country_code.presentation.activity.PhoneCodePickerActivity
import com.tokopedia.travel.country_code.presentation.fragment.PhoneCodePickerFragment
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.travel.passenger.R
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel
import com.tokopedia.travel.passenger.data.entity.TravelUpsertContactModel
import com.tokopedia.travel.passenger.databinding.FragmentTravelContactDataBinding
import com.tokopedia.travel.passenger.di.TravelPassengerComponent
import com.tokopedia.travel.passenger.presentation.activity.TravelContactDataActivity
import com.tokopedia.travel.passenger.presentation.adapter.TravelContactArrayAdapter
import com.tokopedia.travel.passenger.presentation.model.TravelContactData
import com.tokopedia.travel.passenger.presentation.viewmodel.TravelContactDataViewModel
import com.tokopedia.travel.passenger.util.MutationUpsertContact
import com.tokopedia.travel.passenger.util.QueryGetContactList
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class TravelContactDataFragment : BaseDaggerFragment(), TravelContactArrayAdapter.ContactArrayListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var contactViewModel: TravelContactDataViewModel

    private var binding by autoClearedNullable<FragmentTravelContactDataBinding>()

    lateinit var contactData: TravelContactData
    var selectedContact = TravelContactListModel.Contact()

    lateinit var spinnerAdapter: ArrayAdapter<String>
    private val spinnerData = mutableListOf<String>()

    lateinit var travelContactArrayAdapter: TravelContactArrayAdapter
    var travelProduct: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProvider(this, viewModelFactory)
            contactViewModel = viewModelProvider.get(TravelContactDataViewModel::class.java)
        }

        arguments?.let {
            contactData = it.getParcelable(TravelContactDataActivity.EXTRA_INITIAL_CONTACT_DATA)
                ?: TravelContactData()
            travelProduct = it.getString(TravelContactDataActivity.EXTRA_TRAVEL_PRODUCT) ?: ""
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTravelContactDataBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        contactViewModel.getContactList(QueryGetContactList(), travelProduct)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        contactViewModel.contactListResult.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { contactList ->
                contactList?.let { travelContactArrayAdapter.updateItem(it.toMutableList()) }
            }
        )
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

    @SuppressLint("ClickableViewAccessibility")
    fun initView() {
        binding?.run {
            tilContactName.setLabel(getString(R.string.travel_contact_data_name_title))

            context?.let {
                travelContactArrayAdapter = TravelContactArrayAdapter(it, R.layout.layout_travel_passenger_autocompletetv, arrayListOf(), this@TravelContactDataFragment)
                (tilContactName.mEditText as AutoCompleteTextView).setAdapter(travelContactArrayAdapter)

                (tilContactName.mEditText as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ -> autofillView(travelContactArrayAdapter.getItem(position)) }
            }

            tilContactName.mEditText?.setText(contactData.name)
            tilContactName.setErrorTextAppearance(R.style.ErrorTextAppearance)

            tilContactEmail.textFieldInput.setText(contactData.email)

            tilContactPhoneNumber.textFieldInput.setText(contactData.phone)

            val initialPhoneCode = getString(R.string.phone_code_format, contactData.phoneCode)
            if (contactData.phoneCode != 0) {
                spinnerData += initialPhoneCode
            } else {
                spinnerData += getString(R.string.phone_code_format, DEFAULT_PHONE_CODE_NUMBER_ID)
            }
            context?.run {
                spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerData)
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }
            spContactPhoneCode.adapter = spinnerAdapter
            spContactPhoneCode.setSelection(0)
            spContactPhoneCode.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    startActivityForResult(PhoneCodePickerActivity.getCallingIntent(requireContext()), REQUEST_CODE_PHONE_CODE)
                }
                true
            }

            contactDataButton.setOnClickListener { onSaveButtonClicked() }

            layoutFragment.setOnTouchListener { _, _ ->
                clearAllKeyboardFocus()
                true
            }
        }
    }

    private fun clearAllKeyboardFocus() {
        KeyboardHandler.hideSoftKeyboard(activity)
    }

    private fun autofillView(contact: TravelContactListModel.Contact?) {
        if (contact != null) {
            selectedContact = TravelContactListModel.Contact(fullName = contact.fullName, email = contact.email, phoneNumber = contact.phoneNumber)

            binding?.run {
                tilContactEmail.textFieldInput.setText(contact.email)
                tilContactPhoneNumber.textFieldInput.setText(contact.phoneNumber)

                contactData.phoneCode = contact.phoneCountryCode
                spinnerData.clear()
                if (contact.phoneCountryCode != 0) {
                    spinnerData += getString(R.string.phone_code_format, contact.phoneCountryCode)
                } else {
                    spinnerData += getString(R.string.phone_code_format, DEFAULT_PHONE_CODE_NUMBER_ID)
                }
                spinnerAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun onSaveButtonClicked() {
        binding?.run {
            if (validateData()) {
                contactData.name = tilContactName.mEditText?.text.toString()
                contactData.email = tilContactEmail.textFieldInput.text.toString()
                contactData.phone = tilContactPhoneNumber.textFieldInput.text.toString()
                contactData.phoneCode = (spContactPhoneCode.selectedItem as String).toIntOrZero()

                contactViewModel.updateContactList(
                    MutationUpsertContact(),
                    TravelUpsertContactModel.Contact(
                        fullName = contactData.name,
                        email = contactData.email,
                        phoneNumber = contactData.phone,
                        phoneCountryCode = contactData.phoneCode
                    )
                )

                activity?.run {
                    val intent = Intent()
                    intent.putExtra(EXTRA_CONTACT_DATA, contactData)
                    this.setResult(Activity.RESULT_OK, intent)
                    this.finish()
                }
            }
        }
    }

    private fun validateData(): Boolean {
        var isValid = true
        resetEditTextError()
        binding?.run {
            if (tilContactName.mEditText?.text.isNullOrBlank()) {
                tilContactName.error = getString(R.string.travel_contact_data_name_error)
                isValid = false
            } else if (isNotAplhabetOrSpaceOnly(tilContactName.mEditText?.text.toString())) {
                tilContactName.error = getString(R.string.travel_contact_data_name_alphabet_only)
                isValid = false
            }
            if (!isValidEmail(tilContactEmail.textFieldInput.text.toString())) {
                tilContactEmail.setError(true)
                tilContactEmail.setMessage(getString(R.string.travel_contact_data_email_error))
                isValid = false
            }
            if (tilContactPhoneNumber.textFieldInput.text.length < MIN_PHONE_NUMBER_DIGIT) {
                tilContactPhoneNumber.setError(true)
                tilContactPhoneNumber.setMessage(getString(R.string.travel_contact_data_phone_number_error))
                isValid = false
            }
        }
        return isValid
    }

    private fun isNotAplhabetOrSpaceOnly(string: String): Boolean {
        return !string.matches(Regex("^[a-zA-Z\\s]*$"))
    }

    private fun resetEditTextError() {
        binding?.run {
            tilContactEmail.setMessage("")
            tilContactEmail.setError(false)

            tilContactName.error = ""
            tilContactPhoneNumber.setMessage("")
            tilContactEmail.setError(false)
        }
    }

    private fun isValidEmail(contactEmail: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches() && !contactEmail.contains(".@") && !contactEmail.contains("@.")
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(TravelPassengerComponent::class.java).inject(this)
    }

    override fun getFilterText(): String {
        return binding?.tilContactName?.mEditText?.text.toString()
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
