package com.tokopedia.checkout.data.model.response.changeshippingaddress

import com.google.gson.annotations.SerializedName

data class ChangeShippingAddressDataResponse(
    @SerializedName("success")
    val success: Int = 0,

    @SerializedName("message")
    val messages: List<String>? = emptyList()
)