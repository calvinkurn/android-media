package com.tokopedia.sellerorder.common.errorhandler

import com.crashlytics.android.Crashlytics
import com.tokopedia.sellerorder.BuildConfig
import com.tokopedia.sellerorder.common.exception.SomException
import timber.log.Timber
import java.io.PrintWriter
import java.io.StringWriter

object SomErrorHandler {
    fun logMessage(throwable: Throwable, message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                val stackTrace = StringBuilder()
                var cause: Throwable? = throwable
                while (cause != null) {
                    val stringWriter = StringWriter()
                    cause.printStackTrace(PrintWriter(stringWriter))
                    cause = cause.cause
                    stackTrace.append(stringWriter)
                }
                Crashlytics.log("$message $stackTrace")
            } else {
                Timber.e(throwable, message)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun logExceptionToCrashlytics(throwable: Throwable, message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                val exceptionMessage = "$message - ${throwable.localizedMessage}"
                Crashlytics.logException(SomException(
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
}