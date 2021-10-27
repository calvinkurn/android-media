package com.tokopedia.sellerorder.orderextension.domain.models

import com.google.gson.annotations.SerializedName

data class GetOrderExtensionRequestInfoResponse(
    @SerializedName("data")
    val `data`: Data? = Data()
) {
    data class Data(
        @SerializedName("order_extension_request_info")
        val orderExtensionRequestInfo: OrderExtensionRequestInfo = OrderExtensionRequestInfo()
    ) {
        data class OrderExtensionRequestInfo(
            @SerializedName("data")
            val data: OrderExtensionRequestInfoData = OrderExtensionRequestInfoData()
        ) {

            data class OrderExtensionRequestInfoData(
                @SerializedName("message")
                val message: String? = "",
                @SerializedName("message_code")
                val messageCode: Int? = 0,
                @SerializedName("new_deadline")
                val newDeadline: String? = "",
                @SerializedName("reason")
                val reason: List<Reason>? = listOf(),
                @SerializedName("text")
                val text: String? = ""
            ) {
                data class Reason(
                    @SerializedName("reason_title")
                    val reasonTitle: String = "",
                    @SerializedName("reason_code")
                    val reasonCode: Int = 0,
                    @SerializedName("must_comment")
                    val mustComment: Boolean = false
                )
            }
        }
    }
}