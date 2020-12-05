package com.tokopedia.vouchercreation.common.errorhandler

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.vouchercreation.BuildConfig

object MvcErrorHandler {
    fun logToCrashlytics(throwable: Throwable, message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                val exceptionMessage = "$message - ${throwable.localizedMessage}"
                FirebaseCrashlytics.getInstance().recordException(
                        MvcException(
                                message = exceptionMessage,
                                cause = throwable
                        )
                )
            } else {
                throwable.printStackTrace()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}