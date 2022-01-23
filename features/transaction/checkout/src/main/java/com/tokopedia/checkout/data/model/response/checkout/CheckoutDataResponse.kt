package com.tokopedia.checkout.data.model.response.checkout

import com.google.gson.annotations.SerializedName

data class CheckoutDataResponse(
        @SerializedName("success")
        val success: Int = 0,
        @SerializedName("error")
        val error: String = "",
        @SerializedName("message")
        val message: String = "",
        @SerializedName("data")
        val data: Data = Data()
)