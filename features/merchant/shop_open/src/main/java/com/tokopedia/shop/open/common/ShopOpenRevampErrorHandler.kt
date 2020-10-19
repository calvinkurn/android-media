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

    fun logMessage(title: String, userId: String, throwable: Throwable) {
        val errorMessage = String.format(
                "\"%s.\",\"userId: %s\",\"errorMessage: %s\"",
                title,
                userId,
                throwable.message ?: "")

        Timber.w("P2#SHOP_OPEN#%s", errorMessage)
    }

    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}