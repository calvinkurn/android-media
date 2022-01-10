package com.tokopedia.sellerhome.common.errorhandler

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.sellerhome.BuildConfig
import com.tokopedia.sellerhome.common.exception.SellerHomeException

object SellerHomeErrorHandler {

    internal const val WIDGET_TYPE_KEY = "widget_type"
    internal const val LAYOUT_ID_KEY = "layout_id"
    internal const val SHOP_SHARE_DATA = "seller home shop share data error"
    internal const val SHOP_SHARE_TRACKING = "seller home shop share data tracking"
    internal const val SHOP_LOCATION = "seller home shop location error"
    internal const val SHOP_INFO = "seller home shop info error"
    internal const val UPDATE_WIDGET_ERROR = "seller home update widget error"


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
                    SellerHomeException(
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