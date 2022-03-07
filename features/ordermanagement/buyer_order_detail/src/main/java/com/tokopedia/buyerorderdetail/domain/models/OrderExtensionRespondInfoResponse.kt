package com.tokopedia.buyerorderdetail.domain.models


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OrderExtensionRespondInfoResponse(
    @Expose
    @SerializedName("order_extension_respond_info")
    val data: OrderExtensionRespondInfo = OrderExtensionRespondInfo(),
) {
    data class OrderExtensionRespondInfo(
        @Expose
        @SerializedName("message")
        val message: String = "",
        @Expose
        @SerializedName("message_code")
        val messageCode: Int = 0,
        @Expose
        @SerializedName("new_deadline")
        val newDeadline: String = "",
        @Expose
        @SerializedName("reason")
        val reason: String = "",
        @Expose
        @SerializedName("reject_text")
        val rejectText: String = "",
        @Expose
        @SerializedName("text")
        val text: String = ""
    )
}