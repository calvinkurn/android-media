package com.tokopedia.chatbot.chatbot2.websocket

import kotlinx.coroutines.delay

class ChatbotDefaultWebSocketStateHandler : ChatbotWebSocketStateHandler {

    private var waitingTime = INCREMENTAL_RETRY_TIME
    private var isWaiting = false
    private val lock = Any()

    companion object {
        private const val INCREMENTAL_RETRY_TIME = 1000L
        private const val MAX_RETRY_TIME = 5000L
    }

    override suspend fun scheduleForRetry(onStartRetry: suspend () -> Unit) {
        if (isWaiting) return
        startWaiting()
        finishWaiting(onStartRetry)
        stopWaiting()
    }

    private suspend fun startWaiting() {
        isWaiting = true
        delay(waitingTime)
    }

    private suspend fun finishWaiting(onStartRetry: suspend () -> Unit) {
        onStartRetry.invoke()
        updateWaitingTime()
    }

    private fun updateWaitingTime() {
        synchronized(lock) {
            waitingTime += INCREMENTAL_RETRY_TIME
            if (waitingTime > MAX_RETRY_TIME) {
                waitingTime = MAX_RETRY_TIME
            }
        }
    }

    private fun stopWaiting() {
        isWaiting = false
    }

    override fun retrySucceed() {
        synchronized(lock) {
            waitingTime = INCREMENTAL_RETRY_TIME
        }
    }
}
