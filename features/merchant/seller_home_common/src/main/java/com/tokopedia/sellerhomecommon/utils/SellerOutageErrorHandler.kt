package com.tokopedia.sellerhomecommon.utils

import com.google.gson.Gson
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

object SellerOutageErrorHandler {

    // Scalyr/New Relic Error Keys
    private const val PAGE_TYPE_KEY = "page_type"
    private const val ERROR_TYPE_KEY = "error_type"
    private const val DEVICE_ID_KEY = "device_id"
    private const val MESSAGE_KEY = "message"
    private const val STACKTRACE_KEY = "stacktrace"
    private const val EXTRAS_KEY = "extras"

    private const val ERROR_TAG = "SELLER_HOME_ERROR"

    object PageType {
        const val SELLER_HOME = "sellerHomePage"
        const val MANAGE_PRODUCT = "manageProductPage"
        const val SOM = "SomPage"
        const val OTHER_MENU = "otherMenuPage"
    }

    object ErrorType {
        const val ERROR_LAYOUT = "error_layout"
        const val ERROR_WIDGET = "error_widget"
        const val ERROR_TICKER = "error_ticker"
    }

    fun logExceptionToServer(
        throwable: Throwable,
        errorType: String,
        pageType: String,
        deviceId: String,
        extras: Map<String, Any> = mapOf()
    ) {
        ServerLogger.log(
            Priority.P2,
            ERROR_TAG,
            getSellerOutageErrorMessageMap(throwable, errorType, pageType, deviceId, extras)
        )
    }

    private fun getSellerOutageErrorMessageMap(
        throwable: Throwable,
        errorType: String,
        pageType: String,
        deviceId: String,
        extras: Map<String, Any>
    ): Map<String, String> {
        val stringExtras = Gson().toJson(extras)
        return mutableMapOf(
            ERROR_TYPE_KEY to errorType,
            DEVICE_ID_KEY to deviceId,
            MESSAGE_KEY to throwable.localizedMessage.orEmpty(),
            PAGE_TYPE_KEY to pageType,
            EXTRAS_KEY to stringExtras,
            STACKTRACE_KEY to throwable.stackTraceToString()
        )
    }
}