package com.tokopedia.common.travel.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.travel.R
import com.tokopedia.common.travel.di.CommonTravelComponent
import com.tokopedia.common.travel.presentation.activity.TravelContactDataActivity
import com.tokopedia.common.travel.presentation.model.TravelContactData
import kotlinx.android.synthetic.main.fragment_travel_contact_data.*

class TravelContactDataFragment: BaseDaggerFragment() {

    lateinit var contactData: TravelContactData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            contactData = it.getParcelable(TravelContactDataActivity.EXTRA_INITIAL_CONTACT_DATA)!!
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
    }

    fun initView() {
        setupInitialData()
    }

    private fun setupInitialData() {
        til_contact_name.setLabel(getString(R.string.travel_contact_data_name_title))
        til_contact_name.editText.setText(contactData.name)

        til_contact_email.setLabel(getString(R.string.travel_contact_data_email_title))
        til_contact_email.editText.setText(contactData.email)

        til_contact_phone_number.editText.setText(contactData.phone)

        val initialPhoneCode: String = "+" + contactData.phoneCode
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, listOf(initialPhoneCode, initialPhoneCode))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_contact_phone_code.adapter = adapter
        sp_contact_phone_code.setSelection(0)
//        sp_contact_phone_code.setOnClickListener {  }

        contact_data_button.setOnClickListener { onSaveButtonClicked() }
    }

    private fun onSaveButtonClicked() {
        if (validateData()) {
            contactData.name = til_contact_name.editText.text.toString()
            contactData.email = til_contact_email.editText.text.toString()
            contactData.phoneCode = sp_contact_phone_code.selectedItem.toString().toInt()
            contactData.phone = til_contact_phone_number.editText.text.toString()

            val intent = Intent()
            intent.putExtra(EXTRA_CONTACT_DATA, contactData)
            activity!!.setResult(Activity.RESULT_OK, intent)
            activity!!.finish()
        }
    }

    private fun validateData(): Boolean {
        var isValid = true
        if (til_contact_name.editText.text.isNullOrBlank()) {
            til_contact_name.error = getString(R.string.travel_contact_data_name_error)
            isValid = false
        }
        if (isValidEmail(til_contact_email.editText.text.toString())) {
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
        const val MIN_PHONE_NUMBER_DIGIT = 9

        fun getInstance(contactData: TravelContactData): TravelContactDataFragment =
            TravelContactDataFragment().also {
                it.arguments = Bundle().apply {
                    putParcelable(TravelContactDataActivity.EXTRA_INITIAL_CONTACT_DATA, contactData)
                }
            }
    }
}