package com.tokopedia.chatbot.domain.pojo.submitchatcsat


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
            val isSuccess: Int?,
            @SerializedName("toasterMessage")
            val toasterMessage: String?
        )
    }
}