package com.tokopedia.shop_showcase.common.util

import com.google.firebase.crashlytics.FirebaseCrashlytics

object ShowcaseErrorHandler {

    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}