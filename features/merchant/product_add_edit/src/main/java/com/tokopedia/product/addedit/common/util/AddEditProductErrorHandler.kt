package com.tokopedia.product.addedit.common.util

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.product.addedit.BuildConfig
import timber.log.Timber

/**
 * @author by milhamj on 21/04/20.
 */
object AddEditProductErrorHandler {

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