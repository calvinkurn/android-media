package com.tokopedia.oneclickcheckout.order.data.get

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import java.util.*

data class ShopDataResponse(
        @SuppressLint("Invalid Data Type")
        @SerializedName("shop_id")
        val shopId: Long = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("user_id")
        val userId: Long = 0,
        @SerializedName("shop_name")
        val shopName: String = "",
        @SerializedName("is_gold")
        val isGold: Int = 0,
        @SerializedName("is_gold_badge")
        val isGoldBadge: Boolean = false,
        @SerializedName("is_official")
        val isOfficial: Int = 0,
        @SerializedName("postal_code")
        val postalCode: String = "",
        @SerializedName("latitude")
        val latitude: String = "",
        @SerializedName("longitude")
        val longitude: String = "",
        @SerializedName("district_id")
        val districtId: String = "",
        @SerializedName("city_name")
        val cityName: String = "",
        @SerializedName("shop_shipments")
        val shopShipments: List<OccShopShipment> = emptyList(),
        @SerializedName("gold_merchant")
        val goldMerchant: GoldMerchant = GoldMerchant(),
        @SerializedName("official_store")
        val officialStore: OfficialStore = OfficialStore(),
        @SerializedName("shop_type_info")
        val shopType: ShopTypeResponse = ShopTypeResponse()
)

data class OccShopShipment(
        @SuppressLint("Invalid Data Type")
        @SerializedName("ship_id")
        val shipId: Int = 0,
        @SerializedName("ship_name")
        val shipName: String = "",
        @SerializedName("ship_code")
        val shipCode: String = "",
        @SerializedName("ship_logo")
        val shipLogo: String = "",
        @SerializedName("ship_prods")
        val shipProds: List<OccShipProd> = ArrayList(),
        @SerializedName("is_dropship_enabled")
        val isDropshipEnabled: Int = 0
)

data class OccShipProd(
        @SuppressLint("Invalid Data Type")
        @SerializedName("ship_prod_id")
        val shipProdId: Int = 0,
        @SerializedName("ship_prod_name")
        val shipProdName: String = "",
        @SerializedName("ship_group_name")
        val shipGroupName: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("ship_group_id")
        val shipGroupId: Int = 0,
        @SerializedName("additional_fee")
        val additionalFee: Int = 0,
        @SerializedName("minimum_weight")
        val minimumWeight: Int = 0
)

data class OfficialStore(
        @SerializedName("is_official")
        val isOfficial: Int = 0,
        @SerializedName("os_logo_url")
        val osLogoUrl: String = ""
)

data class GoldMerchant(
        @SerializedName("is_gold")
        val isGold: Int = 0,
        @SerializedName("is_gold_badge")
        val isGoldBadge: Boolean = false,
        @SerializedName("gold_merchant_logo_url")
        val goldMerchantLogoUrl: String = ""
)

data class ShopTypeResponse(
        @SerializedName("shop_tier")
        val shopTier: Int = 0,
        @SerializedName("badge")
        val badge: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("title_fmt")
        val titleFmt: String = ""
)