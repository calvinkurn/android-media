package com.tokopedia.product.addedit.common.util

import com.crashlytics.android.Crashlytics

/**
 * @author by milhamj on 21/04/20.
 */
object AddEditProductErrorHandler {

    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            Crashlytics.logException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}