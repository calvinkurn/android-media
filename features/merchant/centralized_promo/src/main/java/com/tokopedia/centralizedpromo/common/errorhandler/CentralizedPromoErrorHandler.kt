package com.tokopedia.centralizedpromo.common.errorhandler

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.centralizedpromo.BuildConfig
import com.tokopedia.centralizedpromo.common.exception.CentralizedPromoException

object CentralizedPromoErrorHandler {

    // Scalyr/New Relic Error Keys
    private const val ERROR_TYPE_KEY = "error_type"
    private const val DEVICE_ID_KEY = "device_id"
    private const val MESSAGE_KEY = "message"
    private const val STACKTRACE_KEY = "stacktrace"
    private const val EXTRAS_KEY = "extras"

    object ErrorType {
        const val ERROR_LAYOUT = "error_layout"
        const val ERROR_WIDGET = "error_widget"
        const val ERROR_TICKER = "error_ticker"
    }

    fun logException(
        throwable: Throwable,
        message: String
    ) {
        logExceptionToCrashlytics(throwable, message)
    }

    private fun logExceptionToCrashlytics(throwable: Throwable, message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                val exceptionMessage = "$message - ${throwable.localizedMessage}"
                FirebaseCrashlytics.getInstance().recordException(
                    CentralizedPromoException(
                        message = exceptionMessage,
                        cause = throwable
                    )
                )
            } else {
                throwable.printStackTrace()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun logExceptionToServer(
        errorTag: String,
        throwable: Throwable,
        errorType: String,
        deviceId: String,
        extras: Map<String, Any> = mapOf()
    ) {
        ServerLogger.log(
            Priority.P2,
            errorTag,
            getSellerOutageErrorMessageMap(throwable, errorType, deviceId, extras)
        )
    }

    private fun getSellerOutageErrorMessageMap(
        throwable: Throwable,
        errorType: String,
        deviceId: String,
        extras: Map<String, Any>
    ): Map<String, String> {
        val stringExtras = Gson().toJson(extras)
        val mutableMap = mutableMapOf<String, String>()
        with(mutableMap) {
            put(ERROR_TYPE_KEY, errorType)
            put(DEVICE_ID_KEY, deviceId)
            put(MESSAGE_KEY, throwable.localizedMessage.orEmpty())
            if (stringExtras.isNotBlank()) {
                put(EXTRAS_KEY, stringExtras)
            }
            put(STACKTRACE_KEY, throwable.stackTraceToString())
        }
        return mutableMap
    }

}
