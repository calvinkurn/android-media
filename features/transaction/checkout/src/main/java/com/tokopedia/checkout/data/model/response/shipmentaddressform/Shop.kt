package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class Shop(
        @SerializedName("shop_id")
        val shopId: Long = 0,
        @SerializedName("user_id")
        val userId: String = "",
        @SerializedName("shop_name")
        val shopName: String = "",
        @SerializedName("shop_image")
        val shopImage: String = "",
        @SerializedName("shop_url")
        val shopUrl: String = "",
        @SerializedName("shop_status")
        val shopStatus: Int = 0,
        @SerializedName("is_gold")
        val isGold: Int = 0,
        @SerializedName("is_gold_badge")
        val isGoldBadge: Boolean = false,
        @SerializedName("is_official")
        val isOfficial: Int = 0,
        @SerializedName("is_free_returns")
        val isFreeReturns: Int = 0,
        @SerializedName("address_id")
        val addressId: String = "",
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
        @SerializedName("gold_merchant")
        val goldMerchant: GoldMerchant = GoldMerchant(),
        @SerializedName("official_store")
        val officialStore: OfficialStore = OfficialStore(),
        @SerializedName("shop_alert_message")
        val shopAlertMessage: String = ""
)