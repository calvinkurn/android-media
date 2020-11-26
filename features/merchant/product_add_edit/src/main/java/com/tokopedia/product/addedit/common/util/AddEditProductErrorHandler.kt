package com.tokopedia.product.addedit.common.util

import com.crashlytics.android.Crashlytics
import com.tokopedia.product.addedit.BuildConfig
import timber.log.Timber

/**
 * @author by milhamj on 21/04/20.
 */
object AddEditProductErrorHandler {

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