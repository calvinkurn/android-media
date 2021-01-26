package com.tokopedia.review.common.util

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.review.feature.inbox.buyerreview.network.ErrorMessageException

/**
 * @author by milhamj on 1/26/21.
 */

object ReviewErrorHandler {
    @JvmStatic
    fun getErrorMessage(context: Context, t: Throwable): String {
        logExceptionToCrashlytics(t)

        val exceptionMessage: String? = (t as? ErrorMessageException)?.message
        return if (exceptionMessage != null && exceptionMessage.isNotBlank()) {
            exceptionMessage
        } else {
            ErrorHandler.getErrorMessage(context.applicationContext, t)
        }
    }

    @JvmStatic
    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}