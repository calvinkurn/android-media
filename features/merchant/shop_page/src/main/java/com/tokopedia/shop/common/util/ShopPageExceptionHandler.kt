package com.tokopedia.shop.common.util

import com.crashlytics.android.Crashlytics
import com.tokopedia.shop.BuildConfig
import com.tokopedia.shop.common.exception.ShopPageException

object ShopPageExceptionHandler {
    const val ERROR_WHEN_GET_YOUTUBE_DATA = "Error when get YouTube data."

    fun logExceptionToCrashlytics(message: String, throwable: Throwable) {
        try {
            if (!BuildConfig.DEBUG) {
                val exceptionMessage = "$message - ${throwable.localizedMessage}"
                Crashlytics.logException(ShopPageException(
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