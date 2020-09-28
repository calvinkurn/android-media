package com.tokopedia.product.manage.common.util

import com.crashlytics.android.Crashlytics

object ProductManageListErrorHandler {

    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            Crashlytics.logException(t)
        }catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}