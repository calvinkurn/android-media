package com.tokopedia.checkout.data.model.response.shipmentaddressform

import com.google.gson.annotations.SerializedName

data class Button(
    @SerializedName("text")
    val text: String = ""
)
