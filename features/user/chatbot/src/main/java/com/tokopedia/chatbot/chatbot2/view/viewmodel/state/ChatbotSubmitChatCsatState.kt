package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

sealed class ChatbotSubmitChatCsatState {
    data class FailureChatbotSubmitChatCsat(val error: Throwable) : ChatbotSubmitChatCsatState()
    data class HandleChatbotSubmitChatCsatSuccess(val message: String) : ChatbotSubmitChatCsatState()
}
