package com.tokopedia.sellerorder.common

import com.crashlytics.android.Crashlytics
import com.tokopedia.sellerorder.BuildConfig

object SomErrorHandler {
    fun logExceptionToCrashlytics(throwable: Throwable, message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                val exceptionMessage = "$message - ${throwable.localizedMessage}"
                Crashlytics.logException(message = exceptionMessage, cause = throwable)
            } else {
                throwable.printStackTrace()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}