package com.tokopedia.cartrevamp.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.EpharmacyEnablerResponse

data class Shop(
    @SerializedName("shop_alert_message")
    val shopAlertMessage: String = "",
    @SerializedName("shop_id")
    val shopId: String = "",
    @SerializedName("shop_name")
    val shopName: String = "",
    @SerializedName("shop_status")
    val shopStatus: Int = 0,
    @SerializedName("postal_code")
    val postalCode: String = "",
    @SerializedName("latitude")
    val latitude: String = "",
    @SerializedName("longitude")
    val longitude: String = "",
    @SerializedName("district_id")
    val districtId: String = "",
    @SerializedName("shop_type_info")
    val shopTypeInfo: ShopTypeInfo = ShopTypeInfo(),
    @SerializedName("shop_ticker")
    val shopTicker: String = "",
    @SerializedName("maximum_weight_wording")
    val maximumWeightWording: String = "",
    @SerializedName("maximum_shipping_weight")
    val maximumShippingWeight: Double = 0.0,
    @SerializedName("is_tokonow")
    val isTokoNow: Boolean = false,
    @SerializedName("shop_shipments")
    val shopShipments: List<ShopShipment> = emptyList(),
    @SerializedName("enabler_data")
    val enabler: EpharmacyEnablerResponse = EpharmacyEnablerResponse()
)
