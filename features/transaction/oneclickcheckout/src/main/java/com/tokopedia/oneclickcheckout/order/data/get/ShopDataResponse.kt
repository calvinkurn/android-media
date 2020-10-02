package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName
import java.util.*

data class ShopDataResponse(
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
        @SerializedName("shop_shipments")
        val shopShipments: List<OccShopShipment> = emptyList(),
        @SerializedName("gold_merchant")
        val goldMerchant: GoldMerchant = GoldMerchant(),
        @SerializedName("official_store")
        val officialStore: OfficialStore = OfficialStore()
)

data class OccShopShipment(
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
        @SerializedName("ship_prod_id")
        val shipProdId: Int = 0,
        @SerializedName("ship_prod_name")
        val shipProdName: String = "",
        @SerializedName("ship_group_name")
        val shipGroupName: String = "",
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