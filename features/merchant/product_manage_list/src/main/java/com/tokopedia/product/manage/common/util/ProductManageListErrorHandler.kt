package com.tokopedia.product.manage.common.util

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.product.manage.BuildConfig
import java.lang.RuntimeException

object ProductManageListErrorHandler {

    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(t)
        }catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun logExceptionToCrashlytics(throwable: Throwable, message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                val exceptionMessage = "$message - ${throwable.localizedMessage}"
                FirebaseCrashlytics.getInstance().recordException(RuntimeException(
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