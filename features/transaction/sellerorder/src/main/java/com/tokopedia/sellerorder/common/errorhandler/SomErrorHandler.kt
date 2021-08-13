package com.tokopedia.sellerorder.common.errorhandler

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.sellerorder.BuildConfig
import com.tokopedia.sellerorder.common.exception.SomException
import timber.log.Timber
import java.io.PrintWriter
import java.io.StringWriter

object SomErrorHandler {

    fun logExceptionToCrashlytics(throwable: Throwable, message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                val exceptionMessage = "$message - ${throwable.localizedMessage}"
                FirebaseCrashlytics.getInstance().recordException(SomException(
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