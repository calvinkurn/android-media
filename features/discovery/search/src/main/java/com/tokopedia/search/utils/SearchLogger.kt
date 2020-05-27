package com.tokopedia.search.utils

import okhttp3.internal.http2.ConnectionShutdownException
import org.apache.commons.lang3.exception.ExceptionUtils
import timber.log.Timber
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException

internal open class SearchLogger {

    companion object {
        private const val DISCOVERY_SEARCH_ERROR_TAG = "P2#DISCOVERY_SEARCH_ERROR#%s;error=%s"
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
        Timber.w(DISCOVERY_SEARCH_ERROR_TAG, message, stackTrace)
    }
}