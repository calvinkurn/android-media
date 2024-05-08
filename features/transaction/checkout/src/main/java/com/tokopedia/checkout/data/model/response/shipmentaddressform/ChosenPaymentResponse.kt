package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class ChosenPaymentResponse(
    @SerializedName("gateway_code")
    val gatewayCode: String = "",
    @SerializedName("tenure_type")
    val tenureType: Int = 0,
    @SerializedName("option_id")
    val optionId: String = "",
    @SerializedName("metadata")
    val metadata: String = ""
)
