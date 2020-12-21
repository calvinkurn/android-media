package com.tokopedia.seller.menu.common.errorhandler

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.seller.menu.common.BuildConfig
import com.tokopedia.seller.menu.common.exception.SellerMenuException

object SellerMenuErrorHandler {

    const val ERROR_GET_SETTING_SHOP_INFO = "Error when get shop info in other setting."

    fun logExceptionToCrashlytics(throwable: Throwable, message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                val exceptionMessage = "$message - ${throwable.localizedMessage}"
                FirebaseCrashlytics.getInstance().recordException(SellerMenuException(
                        message = exceptionMessage,
                        cause = throwable
                ))
            } else {
                throwable.printStackTrace()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }
}