package com.tokopedia.shareexperience.domain.util

import com.google.gson.Gson
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

object ShareExLogger {
    private const val ERROR_TAG = "SHARE_EXPERIENCE_ERROR"

    private const val DEVICE_ID_KEY = "device_id"
    private const val MESSAGE_KEY = "message"
    private const val STACKTRACE_KEY = "stacktrace"
    private const val EXTRAS_KEY = "extras"
    private const val DESCRIPTION_KEY = "description"

    private val gson by lazy {
        Gson()
    }

    fun logExceptionToServerLogger(
        throwable: Throwable,
        deviceId: String,
        description: String,
        extras: Map<String, Any> = mapOf()
    ) {
        val message = createErrorMessage(throwable, deviceId, description, extras)
        ServerLogger.log(Priority.P2, ERROR_TAG, message)
    }

    private fun createErrorMessage(
        throwable: Throwable,
        deviceId: String,
        description: String,
        extras: Map<String, Any>
    ): Map<String, String> {
        val jsonExtras = if (extras.isNotEmpty()) {
            gson.toJson(extras)
        } else {
            ""
        }
        return mutableMapOf(
            DEVICE_ID_KEY to deviceId,
            MESSAGE_KEY to throwable.localizedMessage.orEmpty(),
            DESCRIPTION_KEY to description,
            EXTRAS_KEY to jsonExtras,
            STACKTRACE_KEY to throwable.stackTraceToString()
        )
    }
}
