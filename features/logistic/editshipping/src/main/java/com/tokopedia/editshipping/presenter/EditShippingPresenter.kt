package com.tokopedia.editshipping.presenter

import android.os.Bundle
import com.tokopedia.editshipping.domain.model.editshipping.EditShippingCouriers
import com.tokopedia.editshipping.domain.model.editshipping.ProvinceCitiesDistrict
import com.tokopedia.editshipping.domain.model.editshipping.ShopShipping
import com.tokopedia.editshipping.domain.model.openshopshipping.OpenShopData
import com.tokopedia.logisticCommon.data.entity.address.DistrictRecommendationAddress
import com.tokopedia.logisticCommon.data.entity.address.Token

/**
 * Created by Kris on 2/23/2016.
 * TOKOPEDIA
 */
interface EditShippingPresenter {

    fun fetchData()

    fun bindDataToView(model: EditShippingCouriers?)

    fun fetchDataByLocation(locationID: String?)

    fun fetchDataByLocationOpenShop(locationID: String?)

    fun fetchDataOpenShop()

    fun bindDataToViewOpenShop(model: OpenShopData?)

    fun setServiceCondition(isChecked: Boolean, serviceIndex: Int, courierIndex: Int)

    fun submitValue()

    fun refreshData()

    fun getShopId(): Int

    fun getCompiledShippingId(): String

    fun validateBo(shopId: Int, compiledShippingId: String)

    fun editShippingParamsValid(): Boolean

    val provinceCityDistrictList: List<ProvinceCitiesDistrict?>?

    val shopInformation: ShopShipping?

    val shopModel: EditShippingCouriers?

    fun setShopModelFromSavedInstance(model: EditShippingCouriers?)

    val openShopModel: OpenShopData?

    fun saveOpenShopModel()

    fun setOpenShopModelFromSavedInstance(model: OpenShopData?)

    fun dataWebViewResource(courierIndex: Int, webViewURL: String?)

    fun setCourierAdditionalOptionConfig(courierIndex: Int?, additionalOptionQueries: String?)

    fun getCourierAdditionalOptionsURL(courierIndex: Int): String?

    fun onViewDestroyed()

    fun setSavedInstance(savedInstance: Bundle?)

    fun passShippingData(): OpenShopData?

    fun savePostalCode(s: String?)

    fun saveAddressArea(s: String?)

    fun setSelectedAddress(address: DistrictRecommendationAddress?)

    fun getselectedAddress(): DistrictRecommendationAddress?

    val token: Token?

    companion object {
        const val COURIER_ORIGIN = "courier_origin"
        const val SHIPMENT_IDS = "shipment_ids"
        const val POSTAL = "postal"
        const val SHOP_POSTAL = "shop_postal"
        const val SHOP_ID = "shop_id"
        const val ADDR_STREET = "addr_street"
        const val LATITUDE = "latitude"
        const val LONGITUDE = "longitude"
        const val OS_TYPE = "os_type"
        const val USER_ID = "user_id"
        const val SERVICE_ID = "service_id"
        const val DISTRICT_ID = "shop_courier_origin"
        const val DISTRICT_AND_CITY = "district_and_city"
        const val SELECTED_ADDRESS = "selected_address"
        const val MINIMUM_WEIGHT_POLICY_CHARACTER_SIZE = 0
    }
}