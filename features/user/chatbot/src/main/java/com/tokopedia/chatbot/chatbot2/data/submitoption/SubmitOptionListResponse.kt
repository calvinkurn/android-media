package com.tokopedia.chatbot.chatbot2.data.submitoption

import com.google.gson.annotations.SerializedName

data class SubmitOptionListResponse(
    @SerializedName("chipSubmitHelpfulQuestions")
    val chipSubmitHelpfulQuestions: ChipSubmitHelpfulQuestions?
) {
    data class ChipSubmitHelpfulQuestions(
        @SerializedName("data")
        val submitOptionData: SubmitOptionData?,
        @SerializedName("messageError")
        val messageError: List<String>?,
        @SerializedName("serverProcessTime")
        val serverProcessTime: String?,
        @SerializedName("status")
        val status: String?
    ) {
        data class SubmitOptionData(
            @SerializedName("isSuccess")
            val isSuccess: Long?
        )
    }
}
