package com.tokopedia.oneclickcheckout.order.data.update

import com.google.gson.annotations.SerializedName
import com.tokopedia.cartcommon.data.response.updatecart.ToasterAction
import com.tokopedia.oneclickcheckout.order.data.get.OccPromptResponse

data class UpdateCartOccGqlResponse(
    @SerializedName("update_cart_occ_multi")
    var response: UpdateCartOccResponse
) {
    fun getErrorMessage(): String? {
        return response.data.messages.firstOrNull() ?: response.errorMessage.firstOrNull()
    }
}

data class UpdateCartOccResponse(
    @SerializedName("error_message")
    val errorMessage: List<String> = emptyList(),
    @SerializedName("status")
    val status: String = "",
    @SerializedName("data")
    val data: UpdateCartDataOcc
)

data class UpdateCartDataOcc(
    @SerializedName("messages")
    val messages: List<String> = emptyList(),
    @SerializedName("success")
    val success: Int = 0,
    @SerializedName("toaster_action")
    val toasterAction: ToasterAction = ToasterAction(),
    @SerializedName("prompt")
    val prompt: OccPromptResponse = OccPromptResponse()
)
