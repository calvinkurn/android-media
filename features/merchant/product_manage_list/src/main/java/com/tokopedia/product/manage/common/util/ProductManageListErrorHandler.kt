package com.tokopedia.product.manage.common.util

import com.google.firebase.crashlytics.FirebaseCrashlytics

object ProductManageListErrorHandler {

    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(t)
        }catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}