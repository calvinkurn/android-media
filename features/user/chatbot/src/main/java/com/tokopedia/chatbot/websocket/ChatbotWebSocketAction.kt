package com.tokopedia.chatbot.websocket

sealed class ChatbotWebSocketAction {
    object SocketOpened : ChatbotWebSocketAction()
    data class NewMessage(val message: ChatWebSocketResponse) : ChatbotWebSocketAction()
    data class Failure(val exception: ChatbotWebSocketException) : ChatbotWebSocketAction()
    data class Closed(val code: Int) : ChatbotWebSocketAction()
}
