package com.tokopedia.sellerorder.common.errorhandler

import com.crashlytics.android.Crashlytics
import com.tokopedia.sellerorder.BuildConfig
import com.tokopedia.sellerorder.common.exception.SomException

object SomErrorHandler {
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