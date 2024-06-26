package com.tokopedia.campaign.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.config.GlobalConfig

data class GetOfferingInfoForBuyerRequestParam(
    @SerializedName("request_header")
    val requestHeader: RequestHeader = RequestHeader(),
    @SerializedName("offer_ids")
    val offerIds: List<Long> = emptyList(),
    @SerializedName("shop_ids")
    val shopIds: List<Long> = emptyList(),
    @SerializedName("product_anchor")
    val productAnchor: ProductAnchor? = null,
    @SerializedName("user_id")
    val userId: Long = 0,
    @SerializedName("user_location")
    val userLocation: UserLocation = UserLocation(),
    @SerializedName("additional_params")
    val additionalParams: AdditionalParams? = null
) {
    data class RequestHeader(
        @SerializedName("source")
        val source: String = "shop",
        @SerializedName("usecase")
        val useCase: String = "offer landing page",
        @SerializedName("version")
        val version: String = GlobalConfig.VERSION_NAME,
        @SerializedName("device")
        val device: String = "android",
        @SerializedName("now_info")
        val nowInfo: OfferingNowInfoParam? = null
    )

    data class ProductAnchor(
        @SerializedName("product_ids")
        val productIds: List<Long> = emptyList(),
        @SerializedName("warehouse_ids")
        val warehouseIds: List<Long> = emptyList()
    )

    data class UserLocation(
        @SerializedName("address_id")
        val addressId: Long = 0,
        @SerializedName("district_id")
        val districtId: Long = 0,
        @SerializedName("postal_code")
        val postalCode: String = "",
        @SerializedName("latitude")
        val latitude: String = "",
        @SerializedName("longitude")
        val longitude: String = "",
        @SerializedName("city_id")
        val cityId: Long = 0
    )

    data class AdditionalParams(
        @SerializedName("shop")
        val isRequestShopData: Boolean = false,
        @SerializedName("mini_cart_warehouse")
        val isRequestMiniCartWH: Boolean = false,
        @SerializedName("nearest_warehouse")
        val isRequestNearestWarehouse: Boolean = true
    )
}
