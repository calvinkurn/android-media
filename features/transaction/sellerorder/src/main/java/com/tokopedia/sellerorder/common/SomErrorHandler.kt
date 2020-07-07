package com.tokopedia.sellerorder.common

import com.crashlytics.android.Crashlytics

object SomErrorHandler {
    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            Crashlytics.logException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}