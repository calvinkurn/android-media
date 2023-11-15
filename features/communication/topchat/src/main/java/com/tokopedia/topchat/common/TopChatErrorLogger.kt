package com.tokopedia.topchat.common

import com.google.gson.Gson
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

object TopChatErrorLogger {

    private const val ERROR_TAG = "TOPCHAT_ERROR"

    // new relic Error Keys
    private const val DEVICE_ID_KEY = "device_id"
    private const val MESSAGE_KEY = "message"
    private const val STACKTRACE_KEY = "stacktrace"
    private const val EXTRAS_KEY = "extras"
    private const val DESCRIPTION_KEY = "description"

    const val PAGE_KEY = "page"

    object PAGE {
        const val TOPCHAT_LIST_TAB = "TOPCHAT_LIST_TAB"
    }

    fun logExceptionToServerLogger(
        pageType: String,
        throwable: Throwable,
        deviceId: String,
        description: String,
        extras: Map<String, Any> = mapOf()
    ) {
        val message = createErrorMessage(pageType, throwable, deviceId, description, extras)
        ServerLogger.log(Priority.P2, ERROR_TAG, message)
    }

    private fun createErrorMessage(
        pageType: String,
        throwable: Throwable,
        deviceId: String,
        description: String,
        extras: Map<String, Any>
    ): Map<String, String> {
        return mutableMapOf(
            PAGE_KEY to pageType,
            DEVICE_ID_KEY to deviceId,
            MESSAGE_KEY to throwable.localizedMessage.orEmpty(),
            DESCRIPTION_KEY to description,
            EXTRAS_KEY to Gson().toJson(extras),
            STACKTRACE_KEY to throwable.stackTraceToString()
        )
    }
}
