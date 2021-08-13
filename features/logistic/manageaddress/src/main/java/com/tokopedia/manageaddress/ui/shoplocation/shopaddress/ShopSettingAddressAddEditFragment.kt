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
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.di.ShopLocationComponent
import com.tokopedia.manageaddress.domain.model.shoplocation.ShopLocationOldUiModel
import com.tokopedia.shop.settings.address.presenter.ShopSettingAddressAddEditPresenter
import com.tokopedia.shop.settings.address.view.listener.ShopSettingAddressAddEditView
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_shop_address_add.*
import javax.inject.Inject

class ShopSettingAddressAddEditFragment: BaseDaggerFragment(), ShopSettingAddressAddEditView {

    private var shopLocationOldUiModel: ShopLocationOldUiModel? = null
    private var isAddNew = true
    private var selectedDistrictId = -1
    private var selectedCityId = -1
    private var selectedProvinceId = -1
    private val zipCodes: MutableList<String> = mutableListOf()
    private val zipCodesAdapter: ArrayAdapter<String>  by lazy {
        ArrayAdapter<String>(requireActivity(), com.tokopedia.design.R.layout.item_autocomplete_text_double_row, com.tokopedia.design.R.id.item, zipCodes)
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

        fun createInstance(shopAddressOldUiModel: ShopLocationOldUiModel?, isAddNew: Boolean) =
                ShopSettingAddressAddEditFragment().also { it.arguments = Bundle().apply {
                    putParcelable(PARAM_EXTRA_SHOP_ADDRESS, shopAddressOldUiModel)
                    putBoolean(PARAM_EXTRA_IS_ADD_NEW, isAddNew)
                }}
    }
    override fun initInjector() {
        getComponent(ShopLocationComponent::class.java).inject(this)
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
        arguments?.let { it ->
            shopLocationOldUiModel = it.getParcelable(PARAM_EXTRA_SHOP_ADDRESS)
            shopLocationOldUiModel?.let {
                selectedProvinceId = it.stateId
                selectedCityId = it.cityId
                selectedDistrictId = it.districtId
            }
            isAddNew = it.getBoolean(PARAM_EXTRA_IS_ADD_NEW, true)
        }

        postal_code.setOnTouchListener { _, _ -> if (!postal_code.isPopupShowing) postal_code.showDropDown()
            false }
        postal_code.setOnItemClickListener { _, _, position, _ -> if (position == 0 && !postal_code.text.toString()[0].isDigit())
            postal_code.setText("")}

        postal_code.setAdapter(zipCodesAdapter)

        edit_text_district.setOnClickListener { gotoDistrictActivity() }
        if (!isAddNew)
            initializeFillData()
    }

    private fun initializeFillData() {
        shopLocationOldUiModel?.let {
            edit_text_name.setText(it.name)
            edit_text_address.setText(it.address)
            val district = "${it.stateName}, ${it.cityName}, ${it.districtName}"
            edit_text_district.setText(district)
            postal_code.setText(it.postalCode.toString())
            if (!TextUtils.isEmpty(it.phone)){
                edit_text_phone.setText(it.phone)
            }
            if (!TextUtils.isEmpty(it.email)){
                edit_text_email.setText(it.email)
            }
            if (!TextUtils.isEmpty(it.fax)){
                edit_text_fax.setText(it.fax)
            }
        }

    }

    private fun isDataValidToSave(): Boolean {
        var valid = true

        if (TextUtils.isEmpty(edit_text_name.text.toString())){
            valid = false
            text_input_layout_name.error = getString(R.string.shop_address_name_required)
        } else if (edit_text_name.text.toString().length > 128){
            valid = false
            text_input_layout_name.error = getString(R.string.shop_address_name_max_length_error)
        }

        if (TextUtils.isEmpty(edit_text_address.text.toString())){
            valid = false
            text_input_layout_address.error = getString(R.string.shop_address_required)
        }
        if (TextUtils.isEmpty(edit_text_district.text.toString())){
            valid = false
            text_input_layout_district.error = getString(R.string.shop_district_required)
        }
        if (TextUtils.isEmpty(postal_code.text.toString())){
            valid = false
            text_input_layout_postal_code.error = getString(R.string.shop_postal_code_required)
        }
        if (!TextUtils.isEmpty(edit_text_email.text.toString()) && !Patterns.EMAIL_ADDRESS.matcher(edit_text_email.text.toString()).matches()){
            valid = false
            text_input_layout_email.error = getString(R.string.shop_email_invalid)
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
                edit_text_district.setText(fullAddress)

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

    private fun populateData(): ShopLocationOldUiModel {
        shopLocationOldUiModel = shopLocationOldUiModel ?: ShopLocationOldUiModel()
        return shopLocationOldUiModel!!.apply {
            name = edit_text_name.text.toString()
            address = edit_text_address.text.toString()
            districtId = selectedDistrictId
            cityId = selectedCityId
            stateId = selectedProvinceId
            postalCode = postal_code.text.toString().toInt()
            phone = edit_text_phone.text.toString()
            email = edit_text_email.text.toString()
            fax = edit_text_fax.text.toString()

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
            Toaster.make(requireView(), ErrorHandler.getErrorMessage(activity, throwable),
                    Snackbar.LENGTH_LONG, Toaster.TYPE_ERROR, getString(R.string.title_retry), View.OnClickListener {
                presenter.saveAddress(populateData(), isAddNew)
            })
    }
}