package com.tokopedia.chatbot.websocket

interface ChatbotWebSocketStateHandler {
    suspend fun scheduleForRetry(onStartRetry : suspend () -> Unit)
    fun retrySucceed()
}