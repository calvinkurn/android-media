package com.tokopedia.chatbot.websocket

sealed class ChatbotWebSocketAction {
    data class NewMessage(val message: ChatWebSocketResponse) : ChatbotWebSocketAction()
    data class Closed(val exception: ChatbotWebSocketException) : ChatbotWebSocketAction()
}
