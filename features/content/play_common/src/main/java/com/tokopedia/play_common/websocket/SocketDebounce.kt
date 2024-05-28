package com.tokopedia.play_common.websocket

import android.os.Handler
import android.os.Looper
import javax.inject.Inject

/**
 * Created by Jonathan Darwin on 28 May 2024
 */
class SocketDebounce @Inject constructor() {

    private var numOfRetry = 0

    fun debounce(callback: () -> Unit) {
        numOfRetry++

        val duration = if (numOfRetry >= 4) {
            MAX_DELAY
        } else {
            DEFAULT_DELAY_MULTIPLIER * numOfRetry
        }

        Handler(Looper.getMainLooper()).postDelayed({
            callback()
        }, duration)
    }

    companion object {
        private const val MAX_DELAY = 10000L
        private const val DEFAULT_DELAY_MULTIPLIER = 3000L
    }
}
