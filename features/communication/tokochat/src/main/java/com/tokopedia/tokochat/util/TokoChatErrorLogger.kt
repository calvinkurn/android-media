package com.tokopedia.tokochat.util

import com.google.gson.Gson
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

object TokoChatErrorLogger {

    //todo need update after add into configru
    private const val ERROR_TAG = "BUYER_FLOW_TOKOCHAT"

    // Scalyr Error Keys
    private const val ERROR_TYPE_KEY = "error_type"
    private const val DEVICE_ID_KEY = "device_id"
    private const val MESSAGE_KEY = "message"
    private const val STACKTRACE_KEY = "stacktrace"
    private const val EXTRAS_KEY = "extras"
    private const val DESCRIPTION_KEY = "description"

    internal const val PAGE_KEY = "page"

    object PAGE {
        internal const val TOKOCHAT = "TokoChat"
    }

    object ErrorDescription {
        internal const val RENDER_PAGE_ERROR = "error render page"
        internal const val POOL_ORDER_PROGRESS_ERROR = "error of pool based order progress"
        internal const val RENDER_ORDER_PROGRESS_ERROR = "error of render order progress"
    }

    object ErrorType {
        internal const val ERROR_PAGE = "error_page"
        internal const val ERROR_POOL_ORDER_PROGRESS = "error_pool_order_progress"
        internal const val ERROR_LOAD_ORDER_PROGRESS = "error_load_order_progress"
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
