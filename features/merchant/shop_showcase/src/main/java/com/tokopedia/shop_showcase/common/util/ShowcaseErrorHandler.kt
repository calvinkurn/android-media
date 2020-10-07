package com.tokopedia.shop_showcase.common.util


import com.crashlytics.android.Crashlytics

object ShowcaseErrorHandler {

    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            Crashlytics.logException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}