package com.tokopedia.purchase_platform.features.cart.data.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author anggaprasetiyo on 31/01/18.
 */

data class Shop(
    @SerializedName("shop_id")
    @Expose
    val shopId: Int = 0,
    @SerializedName("user_id")
    @Expose
    val userId: Int = 0,
    @SerializedName("shop_name")
    @Expose
    val shopName: String = "",
    @SerializedName("shop_image")
    @Expose
    val shopImage: String = "",
    @SerializedName("shop_url")
    @Expose
    val shopUrl: String = "",
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
    val postalCode: String = "",
    @SerializedName("latitude")
    @Expose
    val latitude: String = "",
    @SerializedName("longitude")
    @Expose
    val longitude: String = "",
    @SerializedName("district_id")
    @Expose
    val districtId: Int = 0,
    @SerializedName("district_name")
    @Expose
    val districtName: String = "",
    @SerializedName("origin")
    @Expose
    val origin: Int = 0,
    @SerializedName("address_street")
    @Expose
    val addressStreet: String = "",
    @SerializedName("province_id")
    @Expose
    val provinceId: Int = 0,
    @SerializedName("city_id")
    @Expose
    val cityId: Int = 0,
    @SerializedName("city_name")
    @Expose
    val cityName: String = "",
    @SerializedName("gold_merchant")
    @Expose
    val goldMerchant: GoldMerchant = GoldMerchant(),
    @SerializedName("official_store")
    @Expose
    val officialStore: OfficialStore = OfficialStore()
)
