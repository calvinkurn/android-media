package com.tokopedia.buyerorderdetail.domain.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OrderExtensionRespondResponse(
    @Expose
    @SerializedName("order_extension_respond")
    val `data`: Data = Data()
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