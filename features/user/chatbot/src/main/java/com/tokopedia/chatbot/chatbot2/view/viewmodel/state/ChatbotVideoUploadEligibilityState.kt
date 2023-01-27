package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

sealed class ChatbotVideoUploadEligibilityState {
    data class SuccessVideoUploadEligibility(val isEligible: Boolean) : ChatbotVideoUploadEligibilityState()
    object FailureVideoUploadEligibility : ChatbotVideoUploadEligibilityState()
}
