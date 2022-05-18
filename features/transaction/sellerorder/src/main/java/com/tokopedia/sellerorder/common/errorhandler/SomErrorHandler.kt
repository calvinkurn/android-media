package com.tokopedia.sellerorder.common.errorhandler

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.Gson
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.sellerorder.BuildConfig
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.common.exception.SomException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object SomErrorHandler {

    // Scalyr/New Relic Error Keys
    private const val ERROR_TYPE_KEY = "error_type"
    private const val DEVICE_ID_KEY = "device_id"
    private const val MESSAGE_KEY = "message"
    private const val STACKTRACE_KEY = "stacktrace"
    private const val EXTRAS_KEY = "extras"

    const val SOM_TAG = "SOM_ERROR"

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

    fun logExceptionToCrashlytics(throwable: Throwable, message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                val exceptionMessage = "$message - ${throwable.localizedMessage}"
                FirebaseCrashlytics.getInstance().recordException(SomException(
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

    fun getErrorMessage(throwable: Throwable, context: Context): String {
        return if (throwable is UnknownHostException || throwable is SocketTimeoutException) {
            context.getString(R.string.som_error_message_no_internet_connection)
        } else {
            throwable.message ?: context.getString(R.string.som_error_message_server_fault)
        }
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
            getSellerOutageErrorMessageMap(throwable, errorType, deviceId)
        )
    }

    private fun getSellerOutageErrorMessageMap(
        throwable: Throwable,
        errorType: String,
        deviceId: String,
    ): Map<String, String> {
        val mutableMap = mutableMapOf<String, String>()
        with(mutableMap) {
            put(ERROR_TYPE_KEY, errorType)
            put(DEVICE_ID_KEY, deviceId)
            put(MESSAGE_KEY, throwable.localizedMessage.orEmpty())
            put(STACKTRACE_KEY, throwable.stackTraceToString())
        }
        return mutableMap
    }
}