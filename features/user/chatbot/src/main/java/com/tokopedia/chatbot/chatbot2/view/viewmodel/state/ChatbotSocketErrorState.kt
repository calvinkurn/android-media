package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

sealed class ChatbotSocketErrorState {
    object SocketConnectionError : ChatbotSocketErrorState()
    object SocketConnectionSuccessful : ChatbotSocketErrorState()
}
