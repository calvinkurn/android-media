package com.tokopedia.chatbot.chatbot2.domain.video

import java.io.Serializable

sealed interface ChatbotVideoUploadResult : Serializable {
    data class Success(
        val uploadId: String,
        val videoUrl: String
    ) : ChatbotVideoUploadResult
    data class Error(
        val message: String
    ) : ChatbotVideoUploadResult
}
