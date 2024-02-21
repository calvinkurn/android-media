package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class PaymentWidgetResponse(
    @SerializedName("metadata")
    val metadata: String = "",
    @SerializedName("enable")
    val enable: Boolean = false,
    @SerializedName("error_message")
    val errorMessage: String = ""
)
