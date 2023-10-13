package com.tokopedia.tokochat.config.util

import com.google.gson.Gson
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

object TokoChatErrorLogger {

    private const val ERROR_TAG = "TOKOCHAT_ERROR"

    // new relic Error Keys
    private const val ERROR_TYPE_KEY = "error_type"
    private const val DEVICE_ID_KEY = "device_id"
    private const val MESSAGE_KEY = "message"
    private const val STACKTRACE_KEY = "stacktrace"
    private const val EXTRAS_KEY = "extras"
    private const val DESCRIPTION_KEY = "description"

    const val PAGE_KEY = "page"

    object PAGE {
        const val TOKOCHAT = "TokoChat"
        const val TOKOCHAT_LIST = "TokoChat List"
    }

    object ErrorDescription {
        const val RENDER_PAGE_ERROR = "error render page"
        const val POOL_ORDER_PROGRESS_ERROR = "error of pool based order progress"
        const val RENDER_ORDER_PROGRESS_ERROR = "error of render order progress"
    }

    object ErrorType {
        const val ERROR_PAGE = "error_page"
        const val ERROR_POOL_ORDER_PROGRESS = "error_pool_order_progress"
        const val ERROR_LOAD_ORDER_PROGRESS = "error_load_order_progress"
    }

    fun logExceptionToServerLogger(
        pageType: String,
        throwable: Throwable,
        errorType: String,
        deviceId: String,
        description: String,
        extras: Map<String, Any> = mapOf()
    ) {
        val message = createErrorMessage(pageType, throwable, errorType, deviceId, description, extras)
        ServerLogger.log(Priority.P2, ERROR_TAG, message)
    }

    private fun createErrorMessage(
        pageType: String,
        throwable: Throwable,
        errorType: String,
        deviceId: String,
        description: String,
        extras: Map<String, Any>
    ): Map<String, String> {
        return mutableMapOf(
            PAGE_KEY to pageType,
            ERROR_TYPE_KEY to errorType,
            DEVICE_ID_KEY to deviceId,
            MESSAGE_KEY to throwable.localizedMessage.orEmpty(),
            DESCRIPTION_KEY to description,
            EXTRAS_KEY to Gson().toJson(extras),
            STACKTRACE_KEY to throwable.stackTraceToString()
        )
    }
}
