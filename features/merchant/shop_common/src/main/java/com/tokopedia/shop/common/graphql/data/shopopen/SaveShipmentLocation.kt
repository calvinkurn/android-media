package com.tokopedia.shop.common.graphql.data.shopopen


import com.google.gson.annotations.SerializedName


data class SaveShipmentLocation(
        @SerializedName("ongkirOpenShopShipmentLocation")
        val ongkirOpenShopShipmentLocation: OngkirOpenShopShipmentLocation = OngkirOpenShopShipmentLocation()
)

data class OngkirOpenShopShipmentLocation(
        @SerializedName("data")
        val dataSuccessResponse: SuccessResponse = SuccessResponse(),
        @SerializedName("errors")
        val errors: List<Any> = listOf()
)

data class SuccessResponse(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("success")
        val success: Boolean = false
)