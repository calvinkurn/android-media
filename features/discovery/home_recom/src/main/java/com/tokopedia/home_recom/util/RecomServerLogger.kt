package com.tokopedia.home_recom.util

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import okhttp3.internal.http2.ConnectionShutdownException
import org.apache.commons.lang3.exception.ExceptionUtils
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException

object RecomServerLogger {

    private const val RECOM_INFINITE_TOKONOW_ERROR_TAG = "RECOM_INFINITE_TOKONOW_STATUS"

    const val TYPE_ERROR_GET_MINICART = "recom_infinite_error_minicart"

    fun logWarning(
            type: String?,
            throwable: Throwable?,
            reason: String = "",
            data: String = ""
    ) {
        if (type == null || throwable == null || isExceptionExcluded(throwable)) return

        timberLogWarning(type, ExceptionUtils.getStackTrace(throwable), reason, data)
    }

    private fun isExceptionExcluded(throwable: Throwable): Boolean {
        if (throwable is UnknownHostException) return true
        if (throwable is SocketException) return true
        if (throwable is InterruptedIOException) return true
        if (throwable is ConnectionShutdownException) return true

        return false
    }

    private fun timberLogWarning(type: String, stackTrace: String, reason: String, data: String) {
        ServerLogger.log(Priority.P2, RECOM_INFINITE_TOKONOW_ERROR_TAG, mapOf(
                "type" to type,
                "error" to stackTrace,
                "reason" to reason,
                "data" to data
        ))
    }
}