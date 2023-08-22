package com.tokopedia.buy_more_get_more.olp.data.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.config.GlobalConfig

class GetOfferingProductListRequestParam(
    @SerializedName("request_header")
    val requestHeader: RequestHeader = RequestHeader(),
    @SerializedName("product_anchor")
    val productAnchor: ProductAnchor = ProductAnchor(),
    @SerializedName("offer_ids")
    val offerIds: List<Int> = emptyList(),
    @SerializedName("user_id")
    val userId: Long = 0,
    @SerializedName("user_location")
    val userLocation: UserLocation = UserLocation(),
    @SerializedName("page")
    val page: Int = 0,
    @SerializedName("result_per_page")
    val pageSize: Int = 0,
    @SerializedName("order_by")
    val orderBy: Int = 0

) {
    data class RequestHeader(
        @SerializedName("source")
        val source: String = "shop",
        @SerializedName("usecase")
        val useCase: String = "offer landing page",
        @SerializedName("version")
        val version: String = GlobalConfig.VERSION_NAME,
        @SerializedName("device")
        val device: String = "android"
    )

    data class ProductAnchor(
        @SerializedName("warehouse_ids")
        val warehouseIds: List<Int> = emptyList()
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
}
