package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

sealed class ChatbotSubmitCsatRatingState {
    data class HandleFailureChatbotSubmitCsatRating(val error: Throwable) : ChatbotSubmitCsatRatingState()
    data class SuccessChatbotSubmtiCsatRating(val data: String) : ChatbotSubmitCsatRatingState()
}
