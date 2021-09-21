package com.tokopedia.checkout.data.model.response

import com.google.gson.annotations.SerializedName

data class ReleaseBookingResponse(
        @SerializedName("data")
        var `data`: Data = Data(),
        @SerializedName("error_message")
        var errorMessage: List<String> = listOf(),
        @SerializedName("status")
        var status: String = ""
)

data class Data(
        @SerializedName("message")
        var message: List<String> = listOf(),
        @SerializedName("success")
        var success: Int = 0
)