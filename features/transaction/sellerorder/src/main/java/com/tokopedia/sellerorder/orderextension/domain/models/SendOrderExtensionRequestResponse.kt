package com.tokopedia.sellerorder.orderextension.domain.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SendOrderExtensionRequestResponse(
    @SerializedName("data")
    @Expose
    val `data`: Data = Data()
) {
    data class Data(
        @SerializedName("order_extension_request")
        @Expose
        val orderExtensionRequest: OrderExtensionRequest = OrderExtensionRequest()
    ) {
        data class OrderExtensionRequest(
            @SerializedName("data")
            @Expose
            val data: OrderExtensionRequestData = OrderExtensionRequestData()
        ) {

            data class OrderExtensionRequestData(
                @SerializedName("message")
                @Expose
                val message: String? = "",
                @SerializedName("message_code")
                @Expose
                val messageCode: Int? = 0
            ) {
                companion object {
                    private const val SUCCESS_MESSAGE_CODE = 1
                }

                fun isSuccess() = messageCode == SUCCESS_MESSAGE_CODE
            }
        }
    }
}