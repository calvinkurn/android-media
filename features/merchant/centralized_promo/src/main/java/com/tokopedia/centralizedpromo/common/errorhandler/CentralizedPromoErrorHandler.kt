package com.tokopedia.centralizedpromo.common.errorhandler

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.centralizedpromo.BuildConfig
import com.tokopedia.centralizedpromo.common.exception.CentralizedPromoException

object CentralizedPromoErrorHandler {

    fun logException(
        throwable: Throwable,
        message: String
    ) {
        logExceptionToCrashlytics(throwable, message)
    }

    private fun logExceptionToCrashlytics(throwable: Throwable, message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                val exceptionMessage = "$message - ${throwable.localizedMessage}"
                FirebaseCrashlytics.getInstance().recordException(
                    CentralizedPromoException(
                        message = exceptionMessage,
                        cause = throwable
                    )
                )
            } else {
                throwable.printStackTrace()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}
