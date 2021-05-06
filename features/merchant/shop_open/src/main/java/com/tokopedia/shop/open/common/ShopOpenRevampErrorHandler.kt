package com.tokopedia.shop.open.common

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.shop.open.BuildConfig

/**
 * @author by milhamj on 21/04/20.
 */
object ShopOpenRevampErrorHandler {

    fun logMessage(title: String, userId: String, message: String) {
        ServerLogger.log(Priority.P2, "SHOP_OPEN", mapOf("type" to "$title.", "userId" to userId, "errorMessage" to message))
    }

    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(t)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

}