package com.tokopedia.shop.settings.common.util

import com.crashlytics.android.Crashlytics
import com.tokopedia.shop.settings.BuildConfig
import timber.log.Timber

object ShopSettingsErrorHandler {

    fun logMessage(message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                Crashlytics.log(message)
            } else {
                Timber.e(message)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            Crashlytics.logException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}