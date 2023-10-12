package com.tokopedia.chatbot.chatbot2.data.submitchatcsat

import com.google.gson.annotations.SerializedName

data class ChipSubmitChatCsatResponse(
    @SerializedName("chipSubmitChatCSAT")
    val chipSubmitChatCSAT: ChipSubmitChatCSAT?
) {
    data class ChipSubmitChatCSAT(
        @SerializedName("data")
        val csatSubmitData: CsatSubmitData?,
        @SerializedName("messageError")
        val messageError: List<String>?,
        @SerializedName("serverProcessTime")
        val serverProcessTime: String?,
        @SerializedName("status")
        val status: String?
    ) {
        data class CsatSubmitData(
            @SerializedName("isSuccess")
            val isSuccess: Long?,
            @SerializedName("toasterMessage")
            val toasterMessage: String?
        )
    }
}
