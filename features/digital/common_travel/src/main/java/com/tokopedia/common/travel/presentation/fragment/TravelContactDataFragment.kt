package com.tokopedia.common.travel.presentation.fragment

import android.os.Bundle
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

    fun initView() {
        setupInitialData()
    }

    fun setupInitialData() {
        til_contact_name.setLabel(getString(R.string.travel_contact_data_name_title))
        til_contact_email.setLabel(getString(R.string.travel_contact_data_email_title))

        til_contact_name.editText.setText(contactData.name)
        til_contact_email.editText.setText(contactData.email)
        til_contact_phone_number.editText.setText(contactData.phone)

        val initialPhoneCode: String = "+" + contactData.phoneCode
        val adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, listOf(initialPhoneCode, initialPhoneCode))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_contact_phone_code.adapter = adapter
        sp_contact_phone_code.setSelection(0)
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(CommonTravelComponent::class.java).inject(this)
    }

    companion object {
        fun getInstance(contactData: TravelContactData): TravelContactDataFragment =
            TravelContactDataFragment().also {
                it.arguments = Bundle().apply {
                    putParcelable(TravelContactDataActivity.EXTRA_INITIAL_CONTACT_DATA, contactData)
                }
            }
    }
}