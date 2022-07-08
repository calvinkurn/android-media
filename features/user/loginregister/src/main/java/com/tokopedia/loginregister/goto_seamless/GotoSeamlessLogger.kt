package com.tokopedia.loginregister.goto_seamless

import android.util.Log
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

object GotoSeamlessLogger {
    private const val MAX_STACKTRACE_LENGTH = 1000
    private const val GOTO_SEAMLESS_LOGGER = "GOTO_SEAMLESS_TAG"

    fun logError(method: String, ex: Exception) {
        val data = mapOf(
            "method" to method,
            "error" to Log.getStackTraceString(ex).take(MAX_STACKTRACE_LENGTH)
        )
        ServerLogger.log(Priority.P2, GOTO_SEAMLESS_LOGGER, data)
    }
}