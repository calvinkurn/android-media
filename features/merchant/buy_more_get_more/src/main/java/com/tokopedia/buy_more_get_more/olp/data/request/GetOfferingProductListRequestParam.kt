package com.tokopedia.buy_more_get_more.olp.data.request

import com.google.gson.annotations.SerializedName

class GetOfferingProductListRequestParam(
    @SerializedName("request_header")
    val requestHeader: RequestHeader = RequestHeader(),
    @SerializedName("offer_ids")
    val offerIds: List<Int> = emptyList(),
    @SerializedName("user_id")
    val userId: Int = 0,
    @SerializedName("user_location")
    val userLocation: UserLocation = UserLocation()

) {
    data class RequestHeader(
        @SerializedName("source")
        val source: String = "shop",
        @SerializedName("usecase")
        val useCase: String = "offer landing page",
        @SerializedName("version")
        val version: String = "1.2",
        @SerializedName("device")
        val device: String = "android"
    )

    data class UserLocation(
        @SerializedName("address_id")
        val addressId: Int = 0,
        @SerializedName("district_id")
        val districtId: Int = 0,
        @SerializedName("postal_code")
        val postalCode: Int = 0,
        @SerializedName("latitude")
        val latitude: String = "",
        @SerializedName("longitude")
        val longitude: String = "",
        @SerializedName("city_id")
        val cityId: Int = 0
    )
}
