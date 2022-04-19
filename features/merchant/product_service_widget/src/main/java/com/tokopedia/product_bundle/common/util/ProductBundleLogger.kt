package com.tokopedia.product_bundle.common.util

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

// TODO: implement logger when you already get LOG_TAG
object ProductBundleLogger {
    private const val LOG_TAG = "?"
    private const val LOG_ATTRIBUTE_TYPE = "type"

    fun logError(t: Throwable) {
        val errorMessage = t.localizedMessage.orEmpty()
        logExceptionToCrashlytics(t)
        ServerLogger.log(Priority.P2, LOG_TAG, mapOf(LOG_ATTRIBUTE_TYPE to errorMessage))
    }

    private fun logExceptionToCrashlytics(t: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}