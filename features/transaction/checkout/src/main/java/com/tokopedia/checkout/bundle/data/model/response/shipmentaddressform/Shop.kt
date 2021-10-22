package com.tokopedia.checkout.bundle.data.model.response.shipmentaddressform

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class Shop(
        @SuppressLint("Invalid Data Type")
        @SerializedName("shop_id")
        val shopId: Long = 0,
        @SerializedName("shop_name")
        val shopName: String = "",
        @SerializedName("shop_image")
        val shopImage: String = "",
        @SerializedName("shop_url")
        val shopUrl: String = "",
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
        @SerializedName("district_name")
        val districtName: String = "",
        @SerializedName("origin")
        val origin: Int = 0,
        @SerializedName("address_street")
        val addressStreet: String = "",
        @SerializedName("province_id")
        val provinceId: String = "",
        @SerializedName("city_id")
        val cityId: String = "",
        @SerializedName("city_name")
        val cityName: String = "",
        @SerializedName("shop_alert_message")
        val shopAlertMessage: String = "",
        @SerializedName("shop_type_info")
        val shopTypeInfo: ShopTypeInfo = ShopTypeInfo(),
        @SerializedName("is_tokonow")
        val isTokoNow: Boolean = false,

        // Temporary field to determine value of shop type to be sent as dimension81
        // Need to remove in the future when implementing tracking for PM Pro
        @SerializedName("is_gold")
        val isGold: Int = 0,
        @SerializedName("is_official")
        val isOfficial: Int = 0
)