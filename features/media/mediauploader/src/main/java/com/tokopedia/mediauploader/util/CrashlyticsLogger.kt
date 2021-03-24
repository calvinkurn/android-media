package com.tokopedia.mediauploader.util

import com.google.firebase.crashlytics.FirebaseCrashlytics

object CrashlyticsLogger {

    fun logExceptionToCrashlytics(throwable: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(throwable)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}