package com.tokopedia.topchat.chatlist.domain.websocket

import com.tokopedia.topchat.chatlist.viewmodel.WebSocketViewModel
import kotlinx.coroutines.delay
import timber.log.Timber

class DefaultWebSocketStateHandler: WebSocketStateHandler {

    private var waitingTime = INCREMENTAL_RETRY_TIME
    private var isWaiting = false
    private val lock = Any()

    override suspend fun scheduleForRetry(onStartRetry: suspend () -> Unit) {
        if (isWaiting) return
        startWaiting()
        finishWaiting(onStartRetry)
        stopWaiting()
    }

    override fun retrySucceed() {
        synchronized(lock) {
            waitingTime = INCREMENTAL_RETRY_TIME
        }
    }

    private suspend fun startWaiting() {
        Timber.d("${WebSocketViewModel.TAG} - waiting ws retry for ${waitingTime}")
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

    companion object {
        private const val INCREMENTAL_RETRY_TIME = 1_000L
        private const val MAX_RETRY_TIME = 5_000L
    }
}