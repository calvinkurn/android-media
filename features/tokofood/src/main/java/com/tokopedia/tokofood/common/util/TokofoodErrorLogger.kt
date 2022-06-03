package com.tokopedia.tokofood.common.util

import com.google.gson.Gson
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

object TokofoodErrorLogger {

    private const val ERROR_TAG = "BUYER_FLOW_TOKOFOOD"

    // Scalyr Error Keys
    private const val ERROR_TYPE_KEY = "error_type"
    private const val DEVICE_ID_KEY = "device_id"
    private const val MESSAGE_KEY = "message"
    private const val STACKTRACE_KEY = "stacktrace"
    private const val EXTRAS_KEY = "extras"
    private const val DESCRIPTION_KEY = "description"

    internal const val PAGE_KEY = "page"

    object PAGE {
        internal const val HOME = "HOME"
        internal const val CATEGORY = "CATEGORY"
        internal const val MERCHANT = "MERCHANT"
        internal const val PURCHASE = "PURCHASE"
        internal const val PROMO = "PROMO"
        internal const val POST_PURCHASE = "POST_PURCHASE"
    }

    object ErrorDescription {
        internal const val RENDER_PAGE_ERROR = "error render page"
        internal const val ADD_TO_CART_ERROR = "error add to cart"
        internal const val REMOVE_FROM_CART_ERROR = "error remove from cart"
        internal const val UPDATE_CART_ERROR = "error update cart"
        internal const val PAYMENT_ERROR = "error go to payment"
        internal const val POOL_BASED_ERROR = "error of pool based order status"
        internal const val ERROR_DRIVER_PHONE_NUMBER = "error of get driver phone number"
        internal const val ERROR_ELIGIBLE_SET_ADDRESS = "error of user not eligible set address"
        internal const val ERROR_LOAD_MORE_CATEGORY = "error of load more category"
    }

    object ErrorType {
        internal const val ERROR_PAGE = "error_page"
        internal const val ERROR_ADD_TO_CART = "error_atc"
        internal const val ERROR_REMOVE_FROM_CART = "error_remove"
        internal const val ERROR_UPDATE_CART = "error_update"
        internal const val ERROR_PAYMENT = "error_payment"
        internal const val ERROR_POOL_POST_PURCHASE = "error_pool_post_purchase"
        internal const val ERROR_COMPLETED_ORDER_POST_PURCHASE = "error_completed_order_post_purchase"
        internal const val ERROR_DRIVER_PHONE_NUMBER = "error_driver_phone_number"
        internal const val ERROR_ELIGIBLE_SET_ADDRESS = "error_eligible_set_address"
        internal const val ERROR_LOAD_MORE_CATEGORY = "error_load_more_category"
    }

    fun logExceptionToServerLogger(
        pageType: String,
        throwable: Throwable,
        errorType: String,
        deviceId: String,
        description: String,
        extras: Map<String, Any> = mapOf()
    ) {
        val message = createErrorMessage(pageType, throwable, errorType, deviceId, description, extras)
        ServerLogger.log(Priority.P2, ERROR_TAG, message)
    }

    private fun createErrorMessage(
        pageType: String,
        throwable: Throwable,
        errorType: String,
        deviceId: String,
        description: String,
        extras: Map<String, Any>
    ): Map<String, String> {
        return mutableMapOf(
            PAGE_KEY to pageType,
            ERROR_TYPE_KEY to errorType,
            DEVICE_ID_KEY to deviceId,
            MESSAGE_KEY to throwable.localizedMessage.orEmpty(),
            DESCRIPTION_KEY to description,
            EXTRAS_KEY to Gson().toJson(extras),
            STACKTRACE_KEY to throwable.stackTraceToString()
        )
    }

}