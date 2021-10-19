package com.tokopedia.buyerorderdetail.domain.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OrderExtensionRespondResponse(
    @Expose
    @SerializedName("data")
    val `data`: Data = Data(),
    @Expose
    @SerializedName("status")
    val status: String = ""
) {

    data class Data(
        @Expose
        @SerializedName("message")
        val message: String = "",
        @Expose
        @SerializedName("message_code")
        val messageCode: Int = 0
    )
}