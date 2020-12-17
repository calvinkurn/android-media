package com.tokopedia.shop.open.common

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.shop.open.BuildConfig
import timber.log.Timber

/**
 * @author by milhamj on 21/04/20.
 */
object ShopOpenRevampErrorHandler {

    fun logMessage(message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                FirebaseCrashlytics.getInstance().log(message)
            } else {
                Timber.e(message)
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}