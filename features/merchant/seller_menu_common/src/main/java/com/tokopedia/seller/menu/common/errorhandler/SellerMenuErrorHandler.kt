package com.tokopedia.seller.menu.common.errorhandler

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.seller.menu.common.BuildConfig
import com.tokopedia.seller.menu.common.exception.SellerMenuException

object SellerMenuErrorHandler {

    const val ERROR_GET_SETTING_SHOP_INFO = "Error when get shop info in other setting."
    const val ERROR_GET_ADMIN_ACCESS_ROLE = "Error when get admin access role."
    const val ERROR_RENDER_TITLE = "Error when rendering title."
    const val ERROR_GET_SHOP_TYPE = "Error when get shop type."
    const val ERROR_GET_BEFORE_ON_DATE = "Error when get before on date"


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