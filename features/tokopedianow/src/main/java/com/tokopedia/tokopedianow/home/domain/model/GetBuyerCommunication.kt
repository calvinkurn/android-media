package com.tokopedia.tokopedianow.home.domain.model

import com.google.gson.annotations.SerializedName


data class GetBuyerCommunication(
    @SerializedName("TokonowGetBuyerCommunication")
    val response: GetBuyerCommunicationResponse = GetBuyerCommunicationResponse()
) {

    data class GetBuyerCommunicationResponse(
        @SerializedName("header")
        val header: Header = Header(),
        @SerializedName("data")
        val data: GetBuyerCommunicationData = GetBuyerCommunicationData()
    )

    data class Header(
        @SerializedName("process_time")
        val processTime: Double = 0.0,
        @SerializedName("messages")
        val messages: List<String> = listOf(),
        @SerializedName("reason")
        val reason: String = "",
        @SerializedName("error_code")
        val errorCode: String = ""
    )

    data class GetBuyerCommunicationData(
        @SerializedName("shopDetails")
        val shopDetails: ShopDetails = ShopDetails(),
        @SerializedName("locationDetails")
        val locationDetails: LocationDetails = LocationDetails(),
        @SerializedName("shippingDetails")
        val shippingDetails: ShippingDetails = ShippingDetails(),
        @SerializedName("background")
        val background: Background = Background()
    )

    data class Background(
        @SerializedName("color")
        val color: String = "",
        @SerializedName("animationURL")
        val animationURL: String = "",
        @SerializedName("imageURL")
        val imageURL: String = ""
    )


    data class ShippingDetails(
        @SerializedName("hint")
        val hint: String = "",
        @SerializedName("options")
        val options: List<Options> = listOf()
    )

    data class Options(
        @SerializedName("name")
        val name: String = "",
        @SerializedName("details")
        val details: String = "",
        @SerializedName("available")
        val available: Boolean = false
    )

    data class LocationDetails(
        @SerializedName("status")
        val status: String = "",
        @SerializedName("operationHour")
        val operationHour: String = ""
    )

    data class ShopDetails(
        @SerializedName("logoURL")
        val logoURL: String = "",
        @SerializedName("title")
        val title: String = ""
    )
}
