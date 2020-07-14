package com.tokopedia.product.addedit.common.util

import com.google.firebase.crashlytics.FirebaseCrashlytics

/**
 * @author by milhamj on 21/04/20.
 */
object AddEditProductErrorHandler {

    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}