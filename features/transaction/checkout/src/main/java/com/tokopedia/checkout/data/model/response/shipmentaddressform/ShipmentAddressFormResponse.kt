package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class ShipmentAddressFormResponse(
        @SerializedName("status")
        val status: String = "",

        @SerializedName("error_message")
        val errorMessages: List<String> = emptyList(),

        @SerializedName("data")
        val data: ShipmentAddressFormDataResponse = ShipmentAddressFormDataResponse()
)