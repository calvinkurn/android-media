package com.tokopedia.shop.settings.address.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.core.manage.people.address.model.DistrictRecommendationAddress
import com.tokopedia.shop.common.router.ShopSettingRouter
import com.tokopedia.shop.settings.R
import com.tokopedia.shop.settings.address.data.ShopLocationViewModel
import kotlinx.android.synthetic.main.fragment_shop_address_add.*

class ShopSettingAddressAddEditFragment: BaseDaggerFragment() {
    private var shopLocationViewModel: ShopLocationViewModel? = null
    private var isAddNew = true
    private var selectedDistrictId = -1
    private var selectedCityId = -1
    private var selectedProvinceId = -1
    private val zipCodes: MutableList<String> = mutableListOf()
    private val zipCodesAdapter: ArrayAdapter<String>  by lazy {
        ArrayAdapter(activity, R.layout.item_autocomplete_text_double_row, R.id.item, zipCodes)
    }

    companion object {
        private const val DISTRICT_RECOMMENDATION_REQUEST_CODE = 1
        private const val PARAM_EXTRA_ADDRESS = "district_recommendation_address"
        private const val PARAM_EXTRA_SHOP_ADDRESS = "shop_address"
        private const val PARAM_EXTRA_IS_ADD_NEW = "is_add_new"

        fun createInstance(shopAddressViewModel: ShopLocationViewModel?, isAddNew: Boolean) =
                ShopSettingAddressAddEditFragment().also { it.arguments = Bundle().apply {
                    putParcelable(PARAM_EXTRA_SHOP_ADDRESS, shopAddressViewModel)
                    putBoolean(PARAM_EXTRA_IS_ADD_NEW, isAddNew)
                }}
    }
    override fun initInjector() {
    }

    override fun getScreenName(): String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_address_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            shopLocationViewModel = it.getParcelable(PARAM_EXTRA_SHOP_ADDRESS)
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
        shopLocationViewModel?.let {
            edit_text_name.setText(it.name)
            edit_text_address.setText(it.address)
            edit_text_district.setText("${it.stateName}, ${it.cityName}, ${it.districtName}")
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
        if (activity != null && activity!!.application is ShopSettingRouter){
            startActivityForResult((activity!!.application as ShopSettingRouter).getDistrictRecommendationIntent(activity!!),
                    DISTRICT_RECOMMENDATION_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == DISTRICT_RECOMMENDATION_REQUEST_CODE){
            data?.let {
                val recommendationAddress: DistrictRecommendationAddress? = it.extras.getParcelable(PARAM_EXTRA_ADDRESS)
                if (recommendationAddress != null){
                    val fullAddress = "${recommendationAddress.provinceName}, ${recommendationAddress.cityName}, ${recommendationAddress.districtName}"
                    edit_text_district.setText(fullAddress)
                    selectedProvinceId = recommendationAddress.provinceId
                    selectedCityId = recommendationAddress.cityId
                    selectedDistrictId = recommendationAddress.districtId
                    zipCodes.clear()
                    zipCodes.addAll(recommendationAddress.zipCodes)
                    updateAutoTextZipCodes()
                }
            }
        }
    }

    private fun updateAutoTextZipCodes() {
        val hedader = getString(R.string.header_list_postal_code)
        if (!zipCodes.contains(hedader)) zipCodes.add(0, hedader)
        zipCodesAdapter.notifyDataSetChanged()
    }

    fun saveAddress() {
        if (isDataValidToSave()){

        }
    }

    fun updateAddess() {
        if (isDataValidToSave()){

        }
    }
}