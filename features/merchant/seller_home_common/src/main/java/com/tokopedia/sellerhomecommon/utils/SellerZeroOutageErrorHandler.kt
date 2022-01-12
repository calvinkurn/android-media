package com.tokopedia.sellerhomecommon.utils

import com.google.gson.Gson
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

object SellerZeroOutageErrorHandler {

    // Scalyr/New Relic Error Keys
    private const val ERROR_TYPE_KEY = "error_type"
    private const val DEVICE_ID_KEY = "device_id"
    private const val MESSAGE_KEY = "message"
    private const val STACKTRACE_KEY = "stacktrace"
    private const val EXTRAS_KEY = "extras"

    const val SELLER_HOME_TAG = "SELLER_HOME_ERROR"
    const val PRODUCT_MANAGE_TAG = "PRODUCT_MANAGE_ERROR"
    const val SOM_TAG = "SOM_ERROR"
    const val OTHER_MENU = "OTHER_MENU_ERROR"

    object ProductManageMessage {
        const val FILTER_OPTIONS_ERROR = "product manage filter options response error"
        const val SET_FEATURED_PRODUCT_ERROR = "product manage set featured product error"
        const val GET_POP_UP_INFO_ERROR = "product manage get pop up info error"
        const val EDIT_PRICE_ERROR = "product manage edit price error"
        const val EDIT_STOCK_ERROR = "product manage edit stock error"
        const val MULTI_EDIT_PRODUCT_ERROR = "product manage multi edit product error"
        const val PRODUCT_LIST_RESULT_ERROR = "product manage product list result error"
        const val DELETE_PRODUCT_ERROR = "product manage delete product error"
        const val EDIT_VARIANT_PRICE_ERROR = "product manage edit variant price error"
        const val EDIT_VARIANT_STOCK_ERROR = "product manage edit variant stock error"
        const val GET_ALL_DRAFT_COUNT_ERROR = "product manage get all draft count error"
        const val SET_CASHBACK_ERROR = "product manage set cashback error"
        const val GET_STOCK_REMINDER_ERROR = "product manage get stock reminder error"
        const val CREATE_STOCK_REMINDER_ERROR = "product manage create stock reminder error"
        const val UPDATE_STOCK_REMINDER_ERROR = "product manage update stock reminder error"
    }

    object SomMessage {
        //Order List
        const val GET_ORDER_LIST_ERROR = "som get order list error"
        const val FILTER_DATA_ON_ORDER_LIST_ERROR = "som get filter data on order list error"
        const val BULK_ACCEPT_ORDER_ERROR = "som bulk accept order error"
        const val GET_BULK_ACCEPT_ORDER_STATUS_ERROR = "som get bulk accept order status error"
        const val BULK_REQUEST_PICKUP_ERROR = "som bulk request pickup error"
        const val GET_ADMIN_PERMISSION_ERROR = "som get admin permission error"
        const val GET_TOP_ADS_CATEGORY_ERROR = "som get top ads category error"
        const val GET_TICKERS_ERROR = "som get tickers error"
        const val GET_WAITING_PAYMENT_COUNTER_ERROR = "get waiting for payment order counter error"

        //Filter page on bottomsheet
        const val GET_FILTER_DATA_ERROR = "som get filter data error"

        //Order Detail
        const val GET_ORDER_DETAIL_ERROR = "som get order detail error"
        const val GET_REJECT_REASON_ERROR = "som get reject reason error"

        //Order List + Order Detail
        const val VALIDATE_ORDER_ERROR = "som validate order error"
        const val REJECT_CANCEL_REQUEST_ERROR = "som reject cancel request error"
        const val ACCEPT_ORDER_ERROR = "som accept order error"
        const val REJECT_ORDER_ERROR = "som rejecting cancel order error"
        const val CHANGE_AWB_ERROR = "som change AWB error"

        //Confirm Shipping
        const val CONFIRM_SHIPPING_ERROR = "som confirm shipping error"
        const val CHANGE_COURIER_ERROR = "som change courier error"
        const val GET_COURIER_LIST_ERROR = "som get courier list error"

        //Request pickup
        const val GET_REQUEST_PICKUP_DATA_ERROR = "som get request pickup data error"
        const val REQUEST_PICKUP_ERROR = "som request pickup error"

        //Waiting payment
        const val GET_WAITING_PAYMENT_ORDER_LIST_ERROR =
            "som get waiting waiting payment order list error"
    }

    object ErrorType {
        const val ERROR_LAYOUT = "error_layout"
        const val ERROR_WIDGET = "error_widget"
        const val ERROR_TICKER = "error_ticker"
    }

    fun logExceptionToServer(
        errorTag: String,
        throwable: Throwable,
        errorType: String,
        deviceId: String,
        extras: Map<String, Any> = mapOf()
    ) {
        ServerLogger.log(
            Priority.P2,
            errorTag,
            getSellerOutageErrorMessageMap(throwable, errorType, deviceId, extras)
        )
    }

    private fun getSellerOutageErrorMessageMap(
        throwable: Throwable,
        errorType: String,
        deviceId: String,
        extras: Map<String, Any>
    ): Map<String, String> {
        val stringExtras = Gson().toJson(extras)
        val mutableMap = mutableMapOf<String, String>()
        with(mutableMap) {
            put(ERROR_TYPE_KEY, errorType)
            put(DEVICE_ID_KEY, deviceId)
            put(MESSAGE_KEY, throwable.localizedMessage.orEmpty())
            if (stringExtras.isNotBlank()) {
                put(EXTRAS_KEY, stringExtras)
            }
            put(STACKTRACE_KEY, throwable.stackTraceToString())
        }
        return mutableMap
    }
}