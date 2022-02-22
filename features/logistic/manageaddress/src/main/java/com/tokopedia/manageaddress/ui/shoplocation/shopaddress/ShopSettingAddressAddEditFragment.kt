package com.tokopedia.manageaddress.ui.shoplocation.shopaddress

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
import com.tokopedia.manageaddress.databinding.FragmentShopAddressAddBinding
import com.tokopedia.manageaddress.di.ShopLocationComponent
import com.tokopedia.manageaddress.domain.model.shoplocation.ShopLocationOldUiModel
import com.tokopedia.manageaddress.ui.shoplocation.shopaddress.listener.ShopSettingAddressAddEditView
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class ShopSettingAddressAddEditFragment: BaseDaggerFragment(), ShopSettingAddressAddEditView {

    private var shopLocationOldUiModel: ShopLocationOldUiModel = ShopLocationOldUiModel()
    private var isAddNew = true
    private var selectedDistrictId = -1L
    private var selectedCityId = -1L
    private var selectedProvinceId = -1L
    private var zipCodes: MutableList<String> = ArrayList()
    private var binding by autoClearedNullable<FragmentShopAddressAddBinding>()

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
        binding = FragmentShopAddressAddBinding.inflate(inflater, container, false)
        binding?.root?.requestFocus()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { it ->
            val model = it.getParcelable<ShopLocationOldUiModel>(PARAM_EXTRA_SHOP_ADDRESS)
            if (model != null) {
                shopLocationOldUiModel = model
            }
            shopLocationOldUiModel.let {
                selectedProvinceId = it.stateId
                selectedCityId = it.cityId
                selectedDistrictId = it.districtId
            }
            isAddNew = it.getBoolean(PARAM_EXTRA_IS_ADD_NEW, true)
        }

        binding?.postalCode?.textFieldInput?.let {
            it.setOnTouchListener { _, _ -> if (!(it.isPopupShowing)) it.showDropDown()
                false }
            it.setOnItemClickListener { _, _, position, _ -> if (position == 0 && !it.text.toString()[0].isDigit())
                it.setText("")}
        }

        binding?.editTextDistrict?.textFieldInput?.run {
            isFocusable = false
            isClickable = true
            setOnClickListener { gotoDistrictActivity() } }
        if (!isAddNew)
            initializeFillData()
    }

    private fun initializeFillData() {
        shopLocationOldUiModel.let {
            binding?.run {
                editTextName.textFieldInput.setText(it.name)
                editTextAddress.textFieldInput.setText(it.address)
                val district = "${it.stateName}, ${it.cityName}, ${it.districtName}"
                editTextDistrict.textFieldInput.setText(district)
                postalCode.textFieldInput.setText(it.postalCode.toString())
                if (!TextUtils.isEmpty(it.phone)){
                    editTextPhone.textFieldInput.setText(it.phone)
                }
                if (!TextUtils.isEmpty(it.email)){
                    editTextEmail.textFieldInput.setText(it.email)
                }
                if (!TextUtils.isEmpty(it.fax)){
                    editTextFax.textFieldInput.setText(it.fax)
                }   
            }
        }

    }

    private fun isDataValidToSave(): Boolean {
        var valid = true
        binding?.run {
            editTextName.let {
                if (TextUtils.isEmpty(it.textFieldInput.text.toString())){
                    valid = false
                    it.setError(true)
                    it.setMessage(getString(R.string.shop_address_name_required))
                } else if (it.textFieldInput.text.toString().length > 128){
                    valid = false
                    it.setError(true)
                    it.setMessage(getString(R.string.shop_address_name_max_length_error))
                }
            }

            editTextAddress.let {
                if (TextUtils.isEmpty(it.textFieldInput.text.toString())){
                    valid = false
                    it.setError(true)
                    it.setMessage(getString(R.string.shop_address_required))
                }
            }

            editTextDistrict.let {
                if (TextUtils.isEmpty(it.textFieldInput.text.toString())){
                    valid = false
                    it.setError(true)
                    it.setMessage(getString(R.string.shop_district_required))
                }
            }

            postalCode.let {
                if (TextUtils.isEmpty(it.textFieldInput.text.toString())){
                    valid = false
                    it.setError(true)
                    it.setMessage(getString(R.string.shop_postal_code_required))
                } else if (it.textFieldInput.text.length != 5) {
                    valid = false
                    it.setError(true)
                    it.setMessage(getString(R.string.shop_postal_code_invalid))
                }
            }

            editTextEmail.let {
                if (!TextUtils.isEmpty(it.textFieldInput.text.toString()) && !Patterns.EMAIL_ADDRESS.matcher(it.textFieldInput.text.toString()).matches()){
                    valid = false
                    it.setError(true)
                    it.setMessage(getString(R.string.shop_email_invalid))
                }
            }
        }
        return valid
    }

    private fun gotoDistrictActivity() {
        if (activity != null){
            startActivityForResult(RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.DISTRICT_RECOMMENDATION_SHOP_SETTINGS),
                DISTRICT_RECOMMENDATION_REQUEST_CODE
            )
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
                binding?.editTextDistrict?.textFieldInput?.setText(fullAddress)

                selectedProvinceId = it.getLong(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_PROVINCE_ID, -1L)
                selectedCityId = it.getLong(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_CITY_ID, -1L)
                selectedDistrictId = it.getLong(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_DISTRICT_ID, -1L)
                zipCodes = arrayListOf<String>(getString(R.string.header_list_postal_code)).apply {
                    addAll(it.getStringArrayList(
                            INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODES) ?: arrayListOf())
                }
                updateAutoTextZipCodes()
            }
        }
    }

    private fun updateAutoTextZipCodes() {
        val adapter = ArrayAdapter<String>(requireActivity(), com.tokopedia.design.R.layout.item_autocomplete_text_double_row, com.tokopedia.design.R.id.item, zipCodes)
        binding?.postalCode?.textFieldInput?.setAdapter(adapter)
    }

    fun saveAddEditAddress() {
        if (isDataValidToSave()){
            presenter.saveAddress(populateData(), isAddNew)
        }
    }

    private fun populateData(): ShopLocationOldUiModel {
        return shopLocationOldUiModel.apply {
            name = binding?.editTextName?.textFieldInput?.text.toString()
            address = binding?.editTextAddress?.textFieldInput?.text.toString()
            districtId = selectedDistrictId
            cityId = selectedCityId
            stateId = selectedProvinceId
            postalCode = binding?.postalCode?.textFieldInput?.text.toString().toInt()
            phone = binding?.editTextPhone?.textFieldInput?.text.toString()
            email = binding?.editTextEmail?.textFieldInput?.text.toString()
            fax = binding?.editTextFax?.textFieldInput?.text.toString()

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