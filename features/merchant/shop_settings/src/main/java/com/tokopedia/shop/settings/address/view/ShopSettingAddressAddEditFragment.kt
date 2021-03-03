package com.tokopedia.shop.settings.address.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.address.data.ShopLocationUiModel
import com.tokopedia.shop.settings.address.presenter.ShopSettingAddressAddEditPresenter
import com.tokopedia.shop.settings.address.view.listener.ShopSettingAddressAddEditView
import com.tokopedia.shop.settings.common.di.ShopSettingsComponent
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

class ShopSettingAddressAddEditFragment: BaseDaggerFragment(), ShopSettingAddressAddEditView {

    private var tfName: TextFieldUnify? = null
    private var tfAddress: TextFieldUnify? = null
    private var tfDistrict: TextFieldUnify? = null
    private var tfPostalCode: TextFieldUnify? = null
    private var tfPhone: TextFieldUnify? = null
    private var tfEmail: TextFieldUnify? = null
    private var tfFax: TextFieldUnify? = null
    private var shopLocationUiModel: ShopLocationUiModel? = null
    private var isAddNew = true
    private var selectedDistrictId = -1
    private var selectedCityId = -1
    private var selectedProvinceId = -1
    private val zipCodes: MutableList<String> = mutableListOf()
    private val zipCodesAdapter: ArrayAdapter<String>  by lazy {
        ArrayAdapter<String>(requireActivity(), R.layout.item_auto_complete_text_double_row, R.id.item, zipCodes)
    }

    @Inject lateinit var presenter: ShopSettingAddressAddEditPresenter

    companion object {
        private const val DISTRICT_RECOMMENDATION_REQUEST_CODE = 1

        private val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_ID = "district_id"
        private val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_NAME = "district_name"
        private val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_ID = "city_id"
        private val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_NAME = "city_name"
        private val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_ID = "province_id"
        private val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_NAME = "province_name"
        private val INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODES = "zipcodes"

        private const val PARAM_EXTRA_SHOP_ADDRESS = "shop_address"
        private const val PARAM_EXTRA_IS_ADD_NEW = "is_add_new"
        private const val PARAM_EXTRA_IS_SUCCESS = "is_success"

        fun createInstance(shopAddressUiModel: ShopLocationUiModel?, isAddNew: Boolean) =
                ShopSettingAddressAddEditFragment().also { it.arguments = Bundle().apply {
                    putParcelable(PARAM_EXTRA_SHOP_ADDRESS, shopAddressUiModel)
                    putBoolean(PARAM_EXTRA_IS_ADD_NEW, isAddNew)
                }}
    }
    override fun initInjector() {
        getComponent(ShopSettingsComponent::class.java).inject(this)
        presenter.attachView(this)
    }

    override fun getScreenName(): String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_shop_address_add, container, false)
        v.requestFocus()
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
        arguments?.let { it ->
            shopLocationUiModel = it.getParcelable(PARAM_EXTRA_SHOP_ADDRESS)
            shopLocationUiModel?.let {
                selectedProvinceId = it.stateId
                selectedCityId = it.cityId
                selectedDistrictId = it.districtId
            }
            isAddNew = it.getBoolean(PARAM_EXTRA_IS_ADD_NEW, true)
        }

        tfPostalCode?.textFieldInput?.setOnClickListener {
            if (tfPostalCode?.textFieldInput?.isPopupShowing == true)
                tfPostalCode?.textFieldInput?.showDropDown()
        }
        tfPostalCode?.textFieldInput?.setOnItemClickListener { _, _, position, _ -> if (position == 0 && !tfPostalCode?.textFieldInput?.text.toString()[0].isDigit())
            tfPostalCode?.textFieldInput?.setText("")}

        tfPostalCode?.textFieldInput?.setAdapter(zipCodesAdapter)

        tfDistrict?.textFieldWrapper?.setOnClickListener {
            gotoDistrictActivity()
        }
        if (!isAddNew)
            initializeFillData()
    }

    private fun initializeViews() {
        view?.apply {
            tfName = findViewById(R.id.text_input_name)
            tfAddress = findViewById(R.id.text_input_address)
            tfDistrict = findViewById(R.id.text_input_district)
            tfPostalCode = findViewById(R.id.text_input_postal_code)
            tfPhone = findViewById(R.id.text_input_phone)
            tfEmail = findViewById(R.id.text_input_email)
            tfFax = findViewById(R.id.text_input_fax)
        }
    }

    private fun initializeFillData() {
        shopLocationUiModel?.let {
            tfName?.textFieldInput?.setText(it.name)
            tfAddress?.textFieldInput?.setText(it.address)
            val district = "${it.stateName}, ${it.cityName}, ${it.districtName}"
            tfDistrict?.textFieldInput?.setText(district)
            tfPostalCode?.textFieldInput?.setText(it.postalCode.toString())
            if (!TextUtils.isEmpty(it.phone)){
                tfPhone?.textFieldInput?.setText(it.phone)
            }
            if (!TextUtils.isEmpty(it.email)){
                tfEmail?.textFieldInput?.setText(it.email)
            }
            if (!TextUtils.isEmpty(it.fax)){
                tfFax?.textFieldInput?.setText(it.fax)
            }
        }

    }

    private fun isDataValidToSave(): Boolean {
        var valid = true

        if (TextUtils.isEmpty(tfName?.textFieldInput?.text.toString())){
            valid = false
            tfName?.setError(true)
            tfName?.setMessage(getString(R.string.shop_address_name_required))
        } else if (tfName?.textFieldInput?.text.toString().length > 128){
            valid = false
            tfName?.setError(true)
            tfName?.setMessage(getString(R.string.shop_address_name_max_length_error))
        } else {
            tfName?.setError(false)
            tfName?.setMessage("")
        }

        if (TextUtils.isEmpty(tfAddress?.textFieldInput?.text.toString())){
            valid = false
            tfAddress?.setError(true)
            tfAddress?.setMessage(getString(R.string.shop_address_required))
        } else {
            tfAddress?.setError(false)
            tfAddress?.setMessage("")
        }

        if (TextUtils.isEmpty(tfDistrict?.textFieldInput?.text.toString())){
            valid = false
            tfDistrict?.setError(true)
            tfDistrict?.setMessage(getString(R.string.shop_district_required))
        } else {
            tfDistrict?.setError(false)
            tfDistrict?.setMessage("")
        }

        if (TextUtils.isEmpty(tfPostalCode?.textFieldInput?.text.toString())){
            valid = false
            tfPostalCode?.setError(true)
            tfPostalCode?.setMessage(getString(R.string.shop_postal_code_required))
        } else {
            tfPostalCode?.setError(false)
            tfPostalCode?.setMessage("")
        }

        if (!TextUtils.isEmpty(tfEmail?.textFieldInput?.text.toString()) && !Patterns.EMAIL_ADDRESS.matcher(tfEmail?.textFieldInput?.text.toString()).matches()){
            valid = false
            tfEmail?.setError(true)
            tfEmail?.setMessage(getString(R.string.shop_email_invalid))
        } else {
            tfEmail?.setError(false)
            tfEmail?.setMessage("")
        }

        return valid
    }

    private fun gotoDistrictActivity() {
        if (activity != null){
            startActivityForResult(RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.DISTRICT_RECOMMENDATION_SHOP_SETTINGS),
                DISTRICT_RECOMMENDATION_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == DISTRICT_RECOMMENDATION_REQUEST_CODE){
            if (data == null) return

            data.extras?.let {
                val districtName = it.getString(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_NAME, "")
                val cityName = it.getString(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_NAME, "")
                val provinceName = it.getString(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_NAME, "")

                val fullAddress = "$provinceName, $cityName, $districtName"
                tfDistrict?.textFieldInput?.setText(fullAddress)

                selectedProvinceId = it.getInt(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_ID, -1)
                selectedCityId = it.getInt(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_ID, -1)
                selectedDistrictId = it.getInt(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_ID, -1)
                zipCodes.clear()
                zipCodes.addAll(it.getStringArrayList(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODES) ?: listOf())
                updateAutoTextZipCodes()
            }
        }
    }

    private fun updateAutoTextZipCodes() {
        val hedader = getString(R.string.header_list_postal_code)
        if (!zipCodes.contains(hedader)) zipCodes.add(0, hedader)
        zipCodesAdapter.notifyDataSetChanged()
    }

    fun saveAddEditAddress() {
        if (isDataValidToSave()){
            presenter.saveAddress(populateData(), isAddNew)
        }
    }

    private fun populateData(): ShopLocationUiModel {
        shopLocationUiModel = shopLocationUiModel ?: ShopLocationUiModel()
        return shopLocationUiModel!!.apply {
            name = tfName?.textFieldInput?.text.toString()
            address = tfAddress?.textFieldInput?.text.toString()
            districtId = selectedDistrictId
            cityId = selectedCityId
            stateId = selectedProvinceId
            postalCode = tfPostalCode?.textFieldInput?.text.toString().toInt()
            phone = tfPhone?.textFieldInput?.text.toString()
            email = tfEmail?.textFieldInput?.text.toString()
            fax = tfFax?.textFieldInput?.text.toString()

        }
    }

    override fun onSuccesAddEdit(string: String?) {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().putExtras(Bundle().apply {
                putBoolean(PARAM_EXTRA_IS_SUCCESS, !TextUtils.isEmpty(string))
                putBoolean(PARAM_EXTRA_IS_ADD_NEW, isAddNew)
            }))
            finish()
        }
    }

    override fun onErrorAddEdit(throwable: Throwable?) {
        if (view != null && activity != null)
            view?.let {
                Toaster.make(it, ErrorHandler.getErrorMessage(activity, throwable),
                        Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.title_retry), View.OnClickListener {
                    presenter.saveAddress(populateData(), isAddNew)
                })
            }
    }
}