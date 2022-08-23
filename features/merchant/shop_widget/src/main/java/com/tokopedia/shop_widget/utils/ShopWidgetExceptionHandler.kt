package com.tokopedia.shop_widget.utils

import com.tokopedia.shop_widget.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics

object ShopWidgetExceptionHandler {
    fun logExceptionToCrashlytics(message: String, throwable: Throwable) {
        try {
            if (!BuildConfig.DEBUG) {
                val exceptionMessage = "$message - ${throwable.localizedMessage}"
                FirebaseCrashlytics.getInstance().recordException(ShopWidgetException(
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