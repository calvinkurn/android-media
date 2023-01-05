package com.tokopedia.oneclickcheckout.order.data.get

import com.google.gson.annotations.SerializedName
import java.util.*

class ShopDataResponse(
        @SerializedName("shop_id")
        val shopId: String = "",
        @SerializedName("shop_name")
        val shopName: String = "",
        @SerializedName("shop_alert_message")
        val shopAlertMessage: String = "",
        @SerializedName("shop_ticker")
        val shopTicker: String = "",
        @SerializedName("maximum_weight_wording")
        val maximumWeightWording: String = "",
        @SerializedName("maximum_shipping_weight")
        val maximumShippingWeight: Long = 0,
        @SerializedName("is_gold")
        val isGold: Int = 0,
        @SerializedName("is_official")
        val isOfficial: Int = 0,
        @SerializedName("is_tokonow")
        val isTokoNow: Boolean = false,
        @SerializedName("gold_merchant")
        val goldMerchant: GoldMerchant = GoldMerchant(),
        @SerializedName("official_store")
        val officialStore: OfficialStore = OfficialStore(),
        @SerializedName("shop_type_info")
        val shopType: ShopTypeResponse = ShopTypeResponse(),
        @SerializedName("postal_code")
        val postalCode: String = "",
        @SerializedName("latitude")
        val latitude: String = "",
        @SerializedName("longitude")
        val longitude: String = "",
        @SerializedName("district_id")
        val districtId: String = "",
        @SerializedName("shop_shipments")
        val shopShipments: List<OccShopShipment> = emptyList()
)

class OccShopShipment(
        @SerializedName("ship_id")
        val shipId: String = "",
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

class OccShipProd(
        @SerializedName("ship_prod_id")
        val shipProdId: String = "",
        @SerializedName("ship_prod_name")
        val shipProdName: String = "",
        @SerializedName("ship_group_name")
        val shipGroupName: String = "",
        @SerializedName("ship_group_id")
        val shipGroupId: String = "",
        @SerializedName("additional_fee")
        val additionalFee: Int = 0,
        @SerializedName("minimum_weight")
        val minimumWeight: Int = 0
)

class OfficialStore(
        @SerializedName("is_official")
        val isOfficial: Int = 0,
        @SerializedName("os_logo_url")
        val osLogoUrl: String = ""
)

class GoldMerchant(
        @SerializedName("is_gold")
        val isGold: Int = 0,
        @SerializedName("is_gold_badge")
        val isGoldBadge: Boolean = false,
        @SerializedName("gold_merchant_logo_url")
        val goldMerchantLogoUrl: String = ""
)

class ShopTypeResponse(
        @SerializedName("shop_tier")
        val shopTier: Int = 0,
        @SerializedName("badge")
        val badge: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("title_fmt")
        val titleFmt: String = ""
)
