package com.tokopedia.editshipping.ui

import android.content.Context
import com.tokopedia.editshipping.domain.model.ValidateShippingModel
import com.tokopedia.editshipping.domain.model.editshipping.Courier
import com.tokopedia.editshipping.domain.model.editshipping.ShopShipping
import com.tokopedia.logisticCommon.data.entity.address.DistrictRecommendationAddress

/**
 * Created by Kris on 2/23/2016.
 * TOKOPEDIA
 */
interface EditShippingViewListener {

    val mainContext: Context?

    fun addCourier(courier: Courier?, courierIndex: Int)

    fun setShopDetailedInformation(data: ShopShipping?)

    fun setGeoAddress(address: String?)

    fun setShopLocationData(shopData: ShopShipping?)

    fun validateShowPopup(data: ValidateShippingModel?)

    val districtAndCity: String?

    val zipCode: String?

    val streetAddress: String?

    fun openGeoLocation()

    fun zipCodeEmpty()

    fun noServiceChosen()

    fun finishLoading()

    fun refreshData(messageStatus: String?)

    fun finishStartingFragment()

    fun setLocationProvinceCityDistrict(Province: String?, City: String?, District: String?)

    fun setLocationProvinceCityDistrict()

    fun initializeZipCodes()

    fun locationDialogTimeoutListener()

    fun dismissFragment(messageStatus: String?)

    fun openWebView(webResources: String?, courierIndex: Int)

    fun showErrorToast(error: String?)

    fun onFragmentTimeout()

    fun onFragmentNoConnection()

    fun onShowViewAfterLoading()

    fun showLoading()

    fun openDataWebViewResources(courierIndex: Int)

    fun setServiceCondition(isChecked: Boolean, serviceIndex: Int, courierIndex: Int)

    fun editAddress()

    fun showInfoBottomSheet(information: String?, courierServiceName: String?)

    fun refreshLocationViewListener(updatedShopInfo: ShopShipping?)

    fun refreshLocationViewListener(address: DistrictRecommendationAddress?)

    companion object {
        const val LOCATION_FRAGMENT_REQUEST_CODE = 1
        const val ADDITIONAL_OPTION_REQUEST_CODE = 2
        const val OPEN_SHOP_EDIT_SHIPPING_REQUEST_CODE = 3
        const val SELECTED_LOCATION_ID_KEY = "location_id"
        const val MAP_MODE = "map_mode"
        const val EDIT_SHIPPING_RESULT_KEY = "edit_shipping_result"
        const val MODIFIED_COURIER_INDEX_KEY = "modified_courier_index"
        const val EDIT_SHIPPING_DATA = "edit_shipping_data"
        const val OPEN_MAP_CODE = 10
        const val SETTING_PAGE = 12
        const val CREATE_SHOP_PAGE = 13
        const val CURRENT_COURIER_MODEL = "current_courier_model"
        const val CURRENT_OPEN_SHOP_MODEL = "current_open_shop_model"
        const val RESUME_OPEN_SHOP_DATA_KEY = "resume_open_shop_data_key"
    }
}