package com.tokopedia.digital_checkout.utils

import com.google.firebase.crashlytics.FirebaseCrashlytics

object DigitalCheckoutUtil {

    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}
