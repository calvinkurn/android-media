package com.tokopedia.sellerhome.common.errorhandler

import com.crashlytics.android.Crashlytics
import com.tokopedia.sellerhome.BuildConfig
import com.tokopedia.sellerhome.common.exception.SellerHomeException

object SellerHomeErrorHandler {
    fun logExceptionToCrashlytics(throwable: Throwable, message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                val exceptionMessage = "$message - ${throwable.localizedMessage}"
                Crashlytics.logException(SellerHomeException(
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