package com.tokopedia.checkout.data.model.response.shipmentaddressform

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.data.response.EpharmacyEnablerResponse

data class Shop(
    @SuppressLint("Invalid Data Type")
    @SerializedName("shop_id")
    val shopId: Long = 0,
    @SerializedName("shop_name")
    val shopName: String = "",
    @SerializedName("postal_code")
    val postalCode: String = "",
    @SerializedName("latitude")
    val latitude: String = "",
    @SerializedName("longitude")
    val longitude: String = "",
    @SerializedName("district_id")
    val districtId: String = "",
    @SerializedName("shop_alert_message")
    val shopAlertMessage: String = "",
    @SerializedName("shop_type_info")
    val shopTypeInfo: ShopTypeInfo = ShopTypeInfo(),
    @SerializedName("is_tokonow")
    val isTokoNow: Boolean = false,
    @SerializedName("shop_ticker_title")
    val shopTickerTitle: String = "",
    @SerializedName("shop_ticker")
    val shopTicker: String = "",

    // Temporary field to determine value of shop type to be sent as dimension81
    // Need to remove in the future when implementing tracking for PM Pro
    @SerializedName("is_gold")
    val isGold: Int = 0,
    @SerializedName("is_official")
    val isOfficial: Int = 0,

    @SerializedName("enabler_data")
    val enabler: EpharmacyEnablerResponse = EpharmacyEnablerResponse()
)
