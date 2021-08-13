package com.tokopedia.shop.common.util

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.shop.BuildConfig
import com.tokopedia.shop.common.exception.ShopPageException

object ShopPageExceptionHandler {
    const val ERROR_WHEN_GET_YOUTUBE_DATA = "Error when get YouTube data."
    const val ERROR_RECYCLER_VIEW = "Error recycler view."
    const val ERROR_WHEN_GET_MERCHANT_VOUCHER_DATA = "Error when get merchant voucher data"
    const val ERROR_WHEN_UPDATE_FOLLOW_SHOP_DATA = "Error when get follow shop data"

    fun logExceptionToCrashlytics(message: String, throwable: Throwable) {
        try {
            if (!BuildConfig.DEBUG) {
                val exceptionMessage = "$message - ${throwable.localizedMessage}"
                FirebaseCrashlytics.getInstance().recordException(ShopPageException(
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