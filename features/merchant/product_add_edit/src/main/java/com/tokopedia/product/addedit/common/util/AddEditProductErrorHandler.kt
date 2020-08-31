package com.tokopedia.product.addedit.common.util

import com.crashlytics.android.Crashlytics
import com.tokopedia.product.addedit.BuildConfig
import timber.log.Timber
import java.io.PrintWriter
import java.io.StringWriter

/**
 * @author by milhamj on 21/04/20.
 */
object AddEditProductErrorHandler {
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

    fun logMessage(message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                Crashlytics.log(message)
            } else {
                Timber.e(message)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            Crashlytics.logException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}