package com.tokopedia.search.utils

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import okhttp3.internal.http2.ConnectionShutdownException
import org.apache.commons.lang3.exception.ExceptionUtils
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException

open class SearchLogger {

    companion object {
        private const val DISCOVERY_SEARCH_ERROR_TAG = "DISCOVERY_SEARCH_ERROR"
        private const val DISCOVERY_SEARCH_TDN_ERROR_TAG = "DISCOVERY_SEARCH_TDN_ERROR"
        private const val DISCOVERY_SEARCH_ANOMALY_TAG = "DISCOVERY_SEARCH_ANOMALY"
    }

    fun logWarning(message: String?, throwable: Throwable?) {
        if (message == null || throwable == null || isExceptionExcluded(throwable)) return

        timberLogWarning(message, ExceptionUtils.getStackTrace(throwable))
    }

    private fun isExceptionExcluded(throwable: Throwable): Boolean {
        if (throwable is UnknownHostException) return true
        if (throwable is SocketException) return true
        if (throwable is InterruptedIOException) return true
        if (throwable is ConnectionShutdownException) return true

        return false
    }

    protected open fun timberLogWarning(message: String, stackTrace: String) {
        ServerLogger.log(Priority.P2, DISCOVERY_SEARCH_ERROR_TAG, mapOf("type" to message, "error" to stackTrace))
    }

    fun logTDNError(throwable: Throwable?) {
        if (throwable == null) return

        ServerLogger.log(Priority.P2, DISCOVERY_SEARCH_TDN_ERROR_TAG, mapOf("error" to ExceptionUtils.getStackTrace(throwable)))
    }

    fun logAnomalyNoKeyword(message: String?) {
        if (message == null) return

        ServerLogger.log(Priority.P2, DISCOVERY_SEARCH_ANOMALY_TAG, mapOf("type" to "No keyword to search result. $message"))
    }
}