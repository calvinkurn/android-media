package com.tokopedia.sellerhome.common.errorhandler

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.sellerhome.BuildConfig
import com.tokopedia.sellerhome.common.exception.SellerHomeException

object SellerHomeErrorHandler {

    internal const val WIDGET_TYPE_KEY = "widget_type"
    internal const val LAYOUT_ID_KEY = "layout_id"

    private const val ERROR_TAG = "SELLER_HOME_ERROR"

    // Scalyr Error Keys
    private const val ERROR_TYPE_KEY = "error_type"
    private const val DEVICE_ID_KEY = "device_id"
    private const val MESSAGE_KEY = "message"
    private const val STACKTRACE_KEY = "stacktrace"
    private const val EXTRAS_KEY = "extras"

    internal object ErrorType {
        const val ERROR_LAYOUT = "error_layout"
        const val ERROR_WIDGET = "error_widget"
        const val ERROR_TICKER = "error_ticker"
    }

    fun logException(throwable: Throwable,
                     message: String,
                     errorType: String = "",
                     deviceId: String = "",
                     extras: String = "") {

        logExceptionToCrashlytics(throwable, message)

        if (errorType.isNotBlank()) {
            logExceptionToScalyr(throwable, errorType, deviceId, extras)
        }
    }

    private fun logExceptionToCrashlytics(throwable: Throwable, message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                val exceptionMessage = "$message - ${throwable.localizedMessage}"
                FirebaseCrashlytics.getInstance().recordException(SellerHomeException(
                        message = exceptionMessage,
                        cause = throwable
                ))
            } else {
                throwable.printStackTrace()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    private fun logExceptionToScalyr(throwable: Throwable,
                                     errorType: String,
                                     deviceId: String,
                                     extras: String) {
        ServerLogger.log(Priority.P2, ERROR_TAG, getSellerHomeErrorMessageMap(throwable, errorType, deviceId, extras))
    }

    private fun getSellerHomeErrorMessageMap(throwable: Throwable,
                                             errorType: String,
                                             deviceId: String,
                                             extras: String): Map<String, String> {
        return mutableMapOf(
                ERROR_TYPE_KEY to errorType,
                DEVICE_ID_KEY to deviceId,
                MESSAGE_KEY to throwable.localizedMessage.orEmpty(),
                STACKTRACE_KEY to throwable.stackTrace.toString(),
                EXTRAS_KEY to extras
        )
    }

}