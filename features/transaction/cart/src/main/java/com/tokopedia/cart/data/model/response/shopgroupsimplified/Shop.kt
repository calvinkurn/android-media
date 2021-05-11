package com.tokopedia.cart.data.model.response.shopgroupsimplified

import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 31/01/18.
 */

data class Shop(
        @SerializedName("shop_alert_message")
        val shopAlertMessage: String = "",
        @SerializedName("shop_id")
        val shopId: Int = 0,
        @SerializedName("user_id")
        val userId: Int = 0,
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
        val addressId: Int = 0,
        @SerializedName("postal_code")
        val postalCode: String = "",
        @SerializedName("latitude")
        val latitude: String = "",
        @SerializedName("longitude")
        val longitude: String = "",
        @SerializedName("district_id")
        val districtId: Int = 0,
        @SerializedName("district_name")
        val districtName: String = "",
        @SerializedName("origin")
        val origin: Int = 0,
        @SerializedName("address_street")
        val addressStreet: String = "",
        @SerializedName("province_id")
        val provinceId: Int = 0,
        @SerializedName("city_id")
        val cityId: Int = 0,
        @SerializedName("city_name")
        val cityName: String = "",
        @SerializedName("gold_merchant")
        val goldMerchant: GoldMerchant = GoldMerchant(),
        @SerializedName("official_store")
        val officialStore: OfficialStore = OfficialStore(),
        @SerializedName("shop_ticker")
        val shopTicker: String = "",
        @SerializedName("maximum_weight_wording")
        val maximumWeightWording: String = "",
        @SerializedName("maximum_shipping_weight")
        val maximumShippingWeight: Double = 0.0
)
