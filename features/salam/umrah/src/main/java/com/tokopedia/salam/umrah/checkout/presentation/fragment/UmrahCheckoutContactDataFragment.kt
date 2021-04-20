package com.tokopedia.salam.umrah.checkout.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.salam.umrah.R
import com.tokopedia.salam.umrah.checkout.data.ContactUser
import com.tokopedia.salam.umrah.checkout.di.UmrahCheckoutComponent
import com.tokopedia.salam.umrah.checkout.presentation.activity.UmrahCheckoutContactDataActivity.Companion.EXTRA_INITIAL_CONTACT_DATA
import com.tokopedia.salam.umrah.checkout.presentation.viewmodel.UmrahCheckoutPilgrimsViewModel
import com.tokopedia.salam.umrah.common.util.DIGIT_EMAIL
import com.tokopedia.travel.country_code.presentation.activity.PhoneCodePickerActivity
import com.tokopedia.travel.country_code.presentation.fragment.PhoneCodePickerFragment
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.travel.passenger.data.entity.TravelContactListModel
import com.tokopedia.travel.passenger.presentation.adapter.TravelContactArrayAdapter
import com.tokopedia.travel.passenger.util.TravelPassengerGqlQuery
import kotlinx.android.synthetic.main.fragment_umrah_checkout_contact_data.*
import kotlinx.android.synthetic.main.widget_umrah_autocomplete_edit_text.view.*
import javax.inject.Inject

/**
 * @author by firman on 27/11/2019
 */

class UmrahCheckoutContactDataFragment : BaseDaggerFragment(), TravelContactArrayAdapter.ContactArrayListener {

    @Inject
    lateinit var umrahCheckoutPilgrimsViewModel: UmrahCheckoutPilgrimsViewModel

    var contactData: ContactUser = ContactUser()
    var selectedContact = TravelContactListModel.Contact()

    lateinit var spinnerAdapter: ArrayAdapter<String>
    val spinnerData = mutableListOf<String>()

    lateinit var travelContactArrayAdapter: TravelContactArrayAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = savedInstanceState ?: arguments
        args?.let {
            contactData = it.getParcelable(EXTRA_INITIAL_CONTACT_DATA)
                    ?: ContactUser()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        umrahCheckoutPilgrimsViewModel.getContactList(TravelPassengerGqlQuery.CONTACT_LIST)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(EXTRA_INITIAL_CONTACT_DATA,contactData)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_umrah_checkout_contact_data, container, false)

    override fun getFilterText(): String {
        return tf_umrah_checkout_contact_phone_number.textFieldInput.text.toString()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        umrahCheckoutPilgrimsViewModel.contactListResult.observe(viewLifecycleOwner, androidx.lifecycle.Observer { contactList ->
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
        ac_umrah_autocomplete_contact_name.setLabel(getString(com.tokopedia.travel.passenger.R.string.travel_contact_data_name_title))
        ac_umrah_autocomplete_contact_name.setHint(getString(com.tokopedia.travel.passenger.R.string.travel_contact_data_name_title))
        context?.let {
            travelContactArrayAdapter = TravelContactArrayAdapter(it, com.tokopedia.travel.passenger.R.layout.layout_travel_passenger_autocompletetv, arrayListOf(), this)
            (ac_umrah_autocomplete_contact_name.ac_umrah_autocomplete as AutoCompleteTextView).setAdapter(travelContactArrayAdapter)

            (ac_umrah_autocomplete_contact_name.ac_umrah_autocomplete as AutoCompleteTextView).setOnItemClickListener { _, _, position, _ ->
                autofillView(travelContactArrayAdapter.getItem(position))
            }

        }

        ac_umrah_autocomplete_contact_name.ac_umrah_autocomplete.apply {
            setText(contactData.name)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(count > MIN_TEXT){
                        ac_umrah_autocomplete_contact_name.til_umrah_autocomplete.error = ""
                    } else {
                        ac_umrah_autocomplete_contact_name.til_umrah_autocomplete.error = getString(R.string.umrah_checkout_contact_name_error)
                    }
                }

            })
        }
        ac_umrah_autocomplete_contact_name.til_umrah_autocomplete.setErrorTextAppearance(com.tokopedia.common.travel.R.style.ErrorTextAppearance)


        tf_umrah_checkout_contact_email.textFieldInput.apply {
            setText(contactData.email)
            setKeyListener(DigitsKeyListener.getInstance(DIGIT_EMAIL));
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(count > MIN_TEXT){
                        tf_umrah_checkout_contact_email.setError(false)
                        tf_umrah_checkout_contact_email.setMessage("")
                    } else {
                        tf_umrah_checkout_contact_email.setError(true)
                        tf_umrah_checkout_contact_email.setMessage(getString(R.string.umrah_checkout_contact_email_error))
                    }
                }

            })
        }

        tf_umrah_checkout_contact_phone_number.textFieldInput.apply {
            setText(contactData.phoneNumber)
            addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {

                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if(start > MIN_PHONE_NUMBER_DIGIT){
                        tf_umrah_checkout_contact_phone_number.setError(false)
                        tf_umrah_checkout_contact_phone_number.setMessage("")
                    } else {
                        tf_umrah_checkout_contact_phone_number.setError(true)
                        tf_umrah_checkout_contact_phone_number.setMessage(getString(R.string.umrah_checkout_contact_phone_error))
                    }
                }
            })
        }


        val initialPhoneCode = getString(com.tokopedia.common.travel.R.string.phone_code_format, initialPhoneCode(contactData.phoneCode))
        spinnerData += initialPhoneCode
        context?.run {
            spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerData)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        sp_umrah_checkout_contact_phone_code.adapter = spinnerAdapter
        sp_umrah_checkout_contact_phone_code.setSelection(0)
        sp_umrah_checkout_contact_phone_code.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                startActivityForResult(PhoneCodePickerActivity.getCallingIntent(requireContext()), REQUEST_CODE_PHONE_CODE)
            }
            true
        }

        btn_umrah_checkout_contact_data_button.setOnClickListener { onSaveButtonClicked() }
    }

    private fun autofillView(contact: TravelContactListModel.Contact?) {
        if (contact != null) {
            selectedContact = TravelContactListModel.Contact(fullName = contact.fullName, email = contact.email, phoneNumber = contact.phoneNumber)

            tf_umrah_checkout_contact_email.textFieldInput.setText(contact.email)
            tf_umrah_checkout_contact_phone_number.textFieldInput.setText(contact.phoneNumber)

            contactData.phoneCode = contact.phoneCountryCode
            spinnerData.clear()
            spinnerData += getString(com.tokopedia.common.travel.R.string.phone_code_format, contact.phoneCountryCode)
            spinnerAdapter.notifyDataSetChanged()
        }
    }

    private fun onSaveButtonClicked() {
        if (validateData()) {
            contactData.name = ac_umrah_autocomplete_contact_name.ac_umrah_autocomplete.text.toString()
            contactData.email = tf_umrah_checkout_contact_email.textFieldInput.text.toString()
            contactData.phoneNumber = tf_umrah_checkout_contact_phone_number.textFieldInput.text.toString()
            contactData.phoneCode = (sp_umrah_checkout_contact_phone_code.selectedItem as String).toInt()


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
        if (ac_umrah_autocomplete_contact_name.ac_umrah_autocomplete.text.isNullOrBlank()) {
            ac_umrah_autocomplete_contact_name.til_umrah_autocomplete.error = getString(R.string.umrah_checkout_contact_name_error)
            isValid = false
        }
        if (!isValidEmail(tf_umrah_checkout_contact_email.textFieldInput.text.toString())) {
            tf_umrah_checkout_contact_email.setError(true)
            tf_umrah_checkout_contact_email.setMessage(getString(R.string.umrah_checkout_contact_email_error))
            isValid = false
        }
        if (tf_umrah_checkout_contact_phone_number.textFieldInput.text.length < MIN_PHONE_NUMBER_DIGIT) {
            tf_umrah_checkout_contact_phone_number.setMessage(getString(R.string.umrah_checkout_contact_phone_error))
            isValid = false
        }
        return isValid
    }

    private fun isValidEmail(contactEmail: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches() && !contactEmail.contains(".@") && !contactEmail.contains("@.")
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(UmrahCheckoutComponent::class.java).inject(this)
    }

    private fun initialPhoneCode(phoneCode: Int): Int {
        return if (phoneCode == PHONE_CODE_NULL) PHONE_CODE_INDONESIA
        else phoneCode
    }

    companion object {

        const val EXTRA_CONTACT_DATA = "extra_contact_data"
        const val REQUEST_CODE_PHONE_CODE = 300
        const val MIN_PHONE_NUMBER_DIGIT = 9
        const val PHONE_CODE_NULL = 0
        const val PHONE_CODE_INDONESIA = 62

        private const val MIN_TEXT = 0

        fun getInstance(contactData: ContactUser): UmrahCheckoutContactDataFragment =
                UmrahCheckoutContactDataFragment().also {
                    it.arguments = Bundle().apply {
                        putParcelable(EXTRA_INITIAL_CONTACT_DATA, contactData)
                    }
                }
    }
}