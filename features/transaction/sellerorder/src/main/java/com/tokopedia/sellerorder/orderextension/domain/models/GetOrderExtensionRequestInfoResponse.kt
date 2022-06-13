package com.tokopedia.sellerorder.orderextension.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetOrderExtensionRequestInfoResponse(
    @SerializedName("data")
    @Expose
    val `data`: Data? = Data()
) {
    data class Data(
        @SerializedName("order_extension_request_info")
        @Expose
        val orderExtensionRequestInfo: OrderExtensionRequestInfo = OrderExtensionRequestInfo()
    ) {
        data class OrderExtensionRequestInfo(
            @SerializedName("data")
            @Expose
            val data: OrderExtensionRequestInfoData = OrderExtensionRequestInfoData()
        ) {

            data class OrderExtensionRequestInfoData(
                @SerializedName("message")
                @Expose
                val message: String? = "",
                @SerializedName("message_code")
                @Expose
                val messageCode: Int? = 0,
                @SerializedName("new_deadline")
                @Expose
                val newDeadline: String? = "",
                @SerializedName("reason")
                @Expose
                val reason: List<Reason>? = listOf(),
                @SerializedName("text")
                @Expose
                val text: String? = ""
            ) {

                companion object {
                    const val MESSAGE_CODE_SUCCESS = 1
                }

                fun isSuccess(): Boolean {
                    return messageCode == MESSAGE_CODE_SUCCESS
                }

                data class Reason(
                    @SerializedName("reason_title")
                    @Expose
                    val reasonTitle: String = "",
                    @SerializedName("reason_code")
                    @Expose
                    val reasonCode: Int = 0,
                    @SerializedName("must_comment")
                    @Expose
                    val mustComment: Boolean = false
                )
            }
        }
    }
}