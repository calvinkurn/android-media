package com.tokopedia.tokopedianow.common.util

import com.google.gson.Gson
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

object TokoMartHomeErrorLogger {

    private const val ERROR_TAG = "TOKO_MART_HOME_ERROR"

    // Scalyr Error Keys
    private const val ERROR_TYPE_KEY = "error_type"
    private const val DEVICE_ID_KEY = "device_id"
    private const val MESSAGE_KEY = "message"
    private const val STACKTRACE_KEY = "stacktrace"
    private const val EXTRAS_KEY = "extras"
    private const val DESCRIPTION_KEY = "description"

    // Error Description
    internal const val LOAD_LAYOUT_ERROR = "error get layout data"
    internal const val ATC_QUANTITY_ERROR = "error get atc quantity"
    internal const val CHOOSE_ADDRESS_ERROR = "error get choose address"

    internal object ErrorType {
        const val ERROR_LAYOUT = "error_layout"
        const val ERROR_ADD_TO_CART = "error_atc"
        const val ERROR_CHOOSE_ADDRESS = "error_choose_address"
    }

    fun logExceptionToScalyr(
        throwable: Throwable,
        errorType: String,
        deviceId: String,
        description: String,
        extras: Map<String, Any> = mapOf()
    ) {
        val message = createErrorMessage(throwable, errorType, deviceId, description, extras)
        ServerLogger.log(Priority.P2, ERROR_TAG, message)
    }

    private fun createErrorMessage(
        throwable: Throwable,
        errorType: String,
        deviceId: String,
        description: String,
        extras: Map<String, Any>
    ): Map<String, String> {
        return mutableMapOf(
            ERROR_TYPE_KEY to errorType,
            DEVICE_ID_KEY to deviceId,
            MESSAGE_KEY to throwable.localizedMessage.orEmpty(),
            DESCRIPTION_KEY to description,
            EXTRAS_KEY to Gson().toJson(extras),
            STACKTRACE_KEY to throwable.stackTraceToString()
        )
    }
}