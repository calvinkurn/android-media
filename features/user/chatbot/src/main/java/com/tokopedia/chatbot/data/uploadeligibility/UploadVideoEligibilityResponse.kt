package com.tokopedia.chatbot.data.uploadeligibility

import com.google.gson.annotations.SerializedName

data class UploadVideoEligibilityResponse(
    @SerializedName("topbotUploadVideoEligibility")
    val topbotUploadVideoEligibility: TopBotUploadVideoEligibility
) {
    data class TopBotUploadVideoEligibility(
        @SerializedName("header")
        val header: HeaderVideoEligibility,
        @SerializedName("data")
        val data: DataVideoEligibility
    ) {
        data class HeaderVideoEligibility(
            @SerializedName("is_success")
            val isSuccess: Boolean,
            @SerializedName("reason")
            val reason: String,
            @SerializedName("messages")
            val messages: List<String>,
            @SerializedName("error_code")
            val errorCode: String,
        )
        data class DataVideoEligibility(
            @SerializedName("is_eligible")
            val isEligibile: Boolean
        )
    }

}




