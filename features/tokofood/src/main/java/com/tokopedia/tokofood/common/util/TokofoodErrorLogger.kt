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
        internal const val SEARCH = "SEARCH"
    }

    object ErrorDescription {
        internal const val RENDER_PAGE_ERROR = "error render page"
        internal const val REMOVE_FROM_CART_ERROR = "error remove from cart"
        internal const val UPDATE_CART_ERROR = "error update cart"
        internal const val PAYMENT_ERROR = "error go to payment"
        internal const val AGREE_CONSENT_ERROR = "error agree consent"
        internal const val POOL_BASED_ERROR = "error of pool based order status"
        internal const val UNREAD_CHAT_COUNT_ERROR = "error of unread chat count in post purchase"
        internal const val INIT_MUTATION_PROFILE_ERROR = "error of init mutation profile in post purchase"

        internal const val ERROR_DRIVER_PHONE_NUMBER = "error of get driver phone number"
        internal const val ERROR_ELIGIBLE_SET_ADDRESS = "error of user not eligible set address"
        internal const val ERROR_LOAD_MORE_CATEGORY = "error of load more category"
        internal const val ERROR_CHOOSE_ADDRESS_MANAGE_LOCATION = "error of choose_address in manage location"
        internal const val ERROR_CHOOSE_ADDRESS_MERCHANT_PAGE = "error of choose_address in merchant page"
        internal const val ERROR_CHECK_DELIVERY_COVERAGE = "error of deliverage coverage"
        internal const val ERROR_INITIAL_SEARCH_STATE = "error load initial search state"
        internal const val ERROR_REMOVE_RECENT_SEARCH = "error of remove recent search"
        internal const val ERROR_LOAD_SEARCH_RESULT_PAGE = "error load search result page"
        internal const val ERROR_LOAD_FILTER = "error load filter"
    }

    object ErrorType {
        internal const val ERROR_PAGE = "error_page"
        internal const val ERROR_REMOVE_FROM_CART = "error_remove"
        internal const val ERROR_UPDATE_CART = "error_update"
        internal const val ERROR_PAYMENT = "error_payment"
        internal const val ERROR_POOL_POST_PURCHASE = "error_pool_post_purchase"
        internal const val ERROR_UNREAD_CHAT_COUNT_POST_PURCHASE = "error_unread_chat_count"
        internal const val ERROR_COMPLETED_ORDER_POST_PURCHASE = "error_completed_order_post_purchase"
        internal const val INIT_MUTATION_PROFILE_ERROR = "error_init_mutation_profile_post_purchase"

        internal const val ERROR_DRIVER_PHONE_NUMBER = "error_driver_phone_number"
        internal const val ERROR_ELIGIBLE_SET_ADDRESS = "error_eligible_set_address"
        internal const val ERROR_LOAD_MORE_CATEGORY = "error_load_more_category"
        internal const val ERROR_CHOOSE_ADDRESS = "error_choose_address"
        internal const val ERROR_CHECK_DELIVERY_COVERAGE = "error_check_delivery_coverage"
        internal const val ERROR_INITIAL_SEARCH_STATE = "error_load_initial_search_state"
        internal const val ERROR_REMOVE_RECENT_SEARCH = "error_remove_recent_search"
        internal const val ERROR_LOAD_SEARCH_RESULT_PAGE = "error_load_srp"
        internal const val ERROR_LOAD_FILTER = "error_load_filter"
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
