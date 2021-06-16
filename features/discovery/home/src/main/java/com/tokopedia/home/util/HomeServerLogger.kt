package com.tokopedia.home.util

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import okhttp3.internal.http2.ConnectionShutdownException
import org.apache.commons.lang3.exception.ExceptionUtils
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException

object HomeServerLogger {

    private const val DISCOVERY_HOME_STATUS_ERROR_TAG = "HOME_STATUS"

    fun logWarning(type: String?, throwable: Throwable?) {
        if (type == null || throwable == null || isExceptionExcluded(throwable)) return

        timberLogWarning(type, ExceptionUtils.getStackTrace(throwable))
    }

    private fun isExceptionExcluded(throwable: Throwable): Boolean {
        if (throwable is UnknownHostException) return true
        if (throwable is SocketException) return true
        if (throwable is InterruptedIOException) return true
        if (throwable is ConnectionShutdownException) return true

        return false
    }

    private fun timberLogWarning(type: String, stackTrace: String) {
        ServerLogger.log(Priority.P2, DISCOVERY_HOME_STATUS_ERROR_TAG, mapOf("type" to type, "error" to stackTrace))
    }
}