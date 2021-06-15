package com.tokopedia.topchat.chatlist.domain.websocket

interface WebSocketStateHandler {
    suspend fun scheduleForRetry(onStartRetry: suspend () -> Unit)
    fun retrySucceed()
}