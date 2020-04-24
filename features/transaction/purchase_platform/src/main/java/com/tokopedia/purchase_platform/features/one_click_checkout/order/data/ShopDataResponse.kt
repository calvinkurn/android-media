package com.tokopedia.purchase_platform.features.one_click_checkout.order.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopDataResponse(
        @SerializedName("shop_id")
        @Expose
        val shopId: Int = 0,
        @SerializedName("user_id")
        @Expose
        val userId: Int = 0,
        @SerializedName("shop_name")
        @Expose
        val shopName: String? = null,
        @SerializedName("shop_image")
        @Expose
        val shopImage: String? = null,
        @SerializedName("shop_url")
        @Expose
        val shopUrl: String? = null,
        @SerializedName("shop_status")
        @Expose
        val shopStatus: Int = 0,
        @SerializedName("is_gold")
        @Expose
        val isGold: Int = 0,
        @SerializedName("is_gold_badge")
        @Expose
        val isGoldBadge: Boolean = false,
        @SerializedName("is_official")
        @Expose
        val isOfficial: Int = 0,
        @SerializedName("is_free_returns")
        @Expose
        val isFreeReturns: Int = 0,
        @SerializedName("address_id")
        @Expose
        val addressId: Int = 0,
        @SerializedName("postal_code")
        @Expose
        val postalCode: String? = null,
        @SerializedName("latitude")
        @Expose
        val latitude: String? = null,
        @SerializedName("longitude")
        @Expose
        val longitude: String? = null,
        @SerializedName("district_id")
        @Expose
        val districtId: Int = 0,
        @SerializedName("district_name")
        @Expose
        val districtName: String? = null,
        @SerializedName("origin")
        @Expose
        val origin: Int = 0,
        @SerializedName("address_street")
        @Expose
        val addressStreet: String? = null,
        @SerializedName("province_id")
        @Expose
        val provinceId: Int = 0,
        @SerializedName("city_id")
        @Expose
        val cityId: Int = 0,
        @SerializedName("city_name")
        @Expose
        val cityName: String? = null,
        @SerializedName("gold_merchant")
        @Expose
        val goldMerchant: GoldMerchant? = null,
        @SerializedName("official_store")
        @Expose
        val officialStore: OfficialStore? = null,
        @SerializedName("shop_shipments")
        val shopShipments: List<ShopShipment> = emptyList()
)