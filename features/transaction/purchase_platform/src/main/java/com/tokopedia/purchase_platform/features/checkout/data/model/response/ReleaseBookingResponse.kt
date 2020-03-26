package com.tokopedia.purchase_platform.features.checkout.data.model.response
import com.google.gson.annotations.SerializedName

data class ReleaseBookingResponse(
    @SerializedName("data")
    var `data`: Data = Data(),
    @SerializedName("error_messages")
    var errorMessages: List<String> = listOf(),
    @SerializedName("status")
    var status: String = ""
)

data class Data(
    @SerializedName("messages")
    var messages: List<String> = listOf(),
    @SerializedName("success")
    var success: Int = 0
)