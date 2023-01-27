package com.tokopedia.chatbot.chatbot2.data.uploadsecure

import com.google.gson.annotations.SerializedName

data class CheckUploadSecureResponse(
    @SerializedName("topbotUploadSecureAvailability")
    val topbotUploadSecureAvailability: TopbotUploadSecureAvailability
) {
    data class TopbotUploadSecureAvailability(
        @SerializedName("UploadSecureAvailabilityData")
        val uploadSecureAvailabilityData: UploadSecureAvailabilityData
    ) {
        data class UploadSecureAvailabilityData(
            @SerializedName("IsUsingUploadSecure")
            val isUsingUploadSecure: Boolean = false
        )
    }
}
