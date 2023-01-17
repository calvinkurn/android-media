package com.tokopedia.chatbot.util

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