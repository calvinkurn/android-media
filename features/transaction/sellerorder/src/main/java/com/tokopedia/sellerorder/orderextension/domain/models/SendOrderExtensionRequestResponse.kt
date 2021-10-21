package com.tokopedia.sellerorder.orderextension.domain.models

import com.google.gson.annotations.SerializedName

data class SendOrderExtensionRequestResponse(
    @SerializedName("data")
    val `data`: Data = Data()
) {
    data class Data(
        @SerializedName("order_extension_request")
        val orderExtensionRequest: OrderExtensionRequest = OrderExtensionRequest()
    ) {
        data class OrderExtensionRequest(
            @SerializedName("message")
            val message: String? = "",
            @SerializedName("message_code")
            val messageCode: Int? = 0
        ) {
            companion object {
                private const val SUCCESS_MESSAGE_CODE = 1
            }

            fun isSuccess() = messageCode == SUCCESS_MESSAGE_CODE
        }
    }
}