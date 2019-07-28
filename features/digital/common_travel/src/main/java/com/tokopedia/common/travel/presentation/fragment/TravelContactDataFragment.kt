package com.tokopedia.common.travel.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.travel.R
import com.tokopedia.common.travel.di.CommonTravelComponent
import com.tokopedia.common.travel.presentation.activity.PhoneCodePickerActivity
import com.tokopedia.common.travel.presentation.activity.TravelContactDataActivity
import com.tokopedia.common.travel.presentation.model.CountryPhoneCode
import com.tokopedia.common.travel.presentation.model.TravelContactData
import kotlinx.android.synthetic.main.fragment_travel_contact_data.*

class TravelContactDataFragment: BaseDaggerFragment() {

    lateinit var contactData: TravelContactData

    lateinit var spinnerAdapter: ArrayAdapter<String>
    val spinnerData = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            contactData = it.getParcelable(TravelContactDataActivity.EXTRA_INITIAL_CONTACT_DATA) ?: TravelContactData()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_travel_contact_data, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_CODE_PHONE_CODE -> {
                if (resultCode == Activity.RESULT_OK) {
                    val countryPhoneCode = data?.getParcelableExtra(PhoneCodePickerFragment.EXTRA_SELECTED_PHONE_CODE) ?: CountryPhoneCode()
                    contactData.phoneCode = countryPhoneCode.countryPhoneCode.toInt()

                    spinnerData.clear()
                    spinnerData += getString(R.string.phone_code_format, contactData.phoneCode)
                    spinnerAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    fun initView() {
        til_contact_name.setLabel(getString(R.string.travel_contact_data_name_title))
        til_contact_name.editText.setText(contactData.name)
        til_contact_name.setErrorTextAppearance(R.style.ErrorTextAppearance)

        til_contact_email.setLabel(getString(R.string.travel_contact_data_email_title))
        til_contact_email.editText.setText(contactData.email)
        til_contact_email.setErrorTextAppearance(R.style.ErrorTextAppearance)

        til_contact_phone_number.editText.setText(contactData.phone)
        til_contact_phone_number.setErrorTextAppearance(R.style.ErrorTextAppearance)

        val initialPhoneCode = getString(R.string.phone_code_format, contactData.phoneCode)
        spinnerData += initialPhoneCode
        context?.run {
            spinnerAdapter =  ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerData)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        sp_contact_phone_code.adapter = spinnerAdapter
        sp_contact_phone_code.setSelection(0)
        sp_contact_phone_code.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                startActivityForResult(PhoneCodePickerActivity.getCallingIntent(activity), REQUEST_CODE_PHONE_CODE)
            }
            true
        }

        contact_data_button.setOnClickListener { onSaveButtonClicked() }
    }

    private fun onSaveButtonClicked() {
        if (validateData()) {
            contactData.name = til_contact_name.editText.text.toString()
            contactData.email = til_contact_email.editText.text.toString()
            contactData.phone = til_contact_phone_number.editText.text.toString()
            contactData.phoneCode = (sp_contact_phone_code.selectedItem as String).toInt()

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
        if (til_contact_name.editText.text.isNullOrBlank()) {
            til_contact_name.error = getString(R.string.travel_contact_data_name_error)
            isValid = false
        }
        if (!isValidEmail(til_contact_email.editText.text.toString())) {
            til_contact_email.error = getString(R.string.travel_contact_data_email_error)
            isValid = false
        }
        if (til_contact_phone_number.editText.text.length < MIN_PHONE_NUMBER_DIGIT) {
            til_contact_phone_number.error = getString(R.string.travel_contact_data_phone_number_error)
            isValid = false
        }
        return isValid
    }

    private fun isValidEmail(contactEmail: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(contactEmail).matches() && !contactEmail.contains(".@") && !contactEmail.contains("@.")
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(CommonTravelComponent::class.java).inject(this)
    }

    companion object {
        const val EXTRA_CONTACT_DATA = "extra_contact_data"
        const val REQUEST_CODE_PHONE_CODE = 300
        const val MIN_PHONE_NUMBER_DIGIT = 9

        fun getInstance(contactData: TravelContactData): TravelContactDataFragment =
            TravelContactDataFragment().also {
                it.arguments = Bundle().apply {
                    putParcelable(TravelContactDataActivity.EXTRA_INITIAL_CONTACT_DATA, contactData)
                }
            }
    }
}