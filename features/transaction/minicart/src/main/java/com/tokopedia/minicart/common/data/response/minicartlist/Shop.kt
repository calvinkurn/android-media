package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class Shop(
        @SerializedName("address_id")
        val addressId: String = "",
        @SerializedName("address_street")
        val addressStreet: String = "",
        @SerializedName("admin_ids")
        val adminIds: List<String> = emptyList(),
        @SerializedName("city_id")
        val cityId: String = "",
        @SerializedName("city_name")
        val cityName: String = "",
        @SerializedName("country_name")
        val countryName: String = "",
        @SerializedName("district_id")
        val districtId: String = "",
        @SerializedName("district_name")
        val districtName: String = "",
        @SerializedName("gold_merchant")
        val goldMerchant: GoldMerchant = GoldMerchant(),
        @SerializedName("is_allow_manage")
        val isAllowManage: Boolean = false,
        @SerializedName("is_free_returns")
        val isFreeReturns: Int = 0,
        @SerializedName("is_gold")
        val isGold: Int = 0,
        @SerializedName("is_gold_badge")
        val isGoldBadge: Boolean = false,
        @SerializedName("is_official")
        val isOfficial: Int = 0,
        @SerializedName("latitude")
        val latitude: String = "",
        @SerializedName("longitude")
        val longitude: String = "",
        @SerializedName("maximum_shipping_weight")
        val maximumShippingWeight: Int = 0,
        @SerializedName("maximum_weight_wording")
        val maximumWeightWording: String = "",
        @SerializedName("official_store")
        val officialStore: OfficialStore = OfficialStore(),
        @SerializedName("origin")
        val origin: Int = 0,
        @SerializedName("postal_code")
        val postalCode: String = "",
        @SerializedName("province_id")
        val provinceId: String = "",
        @SerializedName("province_name")
        val provinceName: String = "",
        @SerializedName("shop_alert_message")
        val shopAlertMessage: String = "",
        @SerializedName("shop_domain")
        val shopDomain: String = "",
        @SerializedName("shop_id")
        val shopId: String = "",
        @SerializedName("shop_image")
        val shopImage: String = "",
        @SerializedName("shop_name")
        val shopName: String = "",
        @SerializedName("shop_status")
        val shopStatus: Int = 0,
        @SerializedName("shop_ticker")
        val shopTicker: String = "",
        @SerializedName("shop_type_info")
        val shopTypeInfo: ShopTypeInfo = ShopTypeInfo(),
        @SerializedName("shop_url")
        val shopUrl: String = "",
        @SerializedName("user_id")
        val userId: String = ""
)