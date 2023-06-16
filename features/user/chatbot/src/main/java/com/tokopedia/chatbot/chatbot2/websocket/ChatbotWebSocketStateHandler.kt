package com.tokopedia.chatbot.chatbot2.websocket

interface ChatbotWebSocketStateHandler {
    suspend fun scheduleForRetry(onStartRetry: suspend () -> Unit)
    fun retrySucceed()
}
