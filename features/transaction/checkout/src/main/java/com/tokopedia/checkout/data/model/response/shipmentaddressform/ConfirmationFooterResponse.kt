package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class ConfirmationFooterResponse(
    @SerializedName("type")
    val type: String = "",
    @SerializedName("text")
    val text: String = ""
)
