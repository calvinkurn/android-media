package com.tokopedia.logisticseller.common.errorhandler

import android.content.Context
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.logisticseller.BuildConfig
import com.tokopedia.logisticseller.R
import com.tokopedia.logisticseller.common.exception.LogisticSellerException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

object LogisticSellerErrorHandler {

    // Scalyr/New Relic Error Keys
    private const val ERROR_TYPE_KEY = "error_type"
    private const val DEVICE_ID_KEY = "device_id"
    private const val MESSAGE_KEY = "message"
    private const val STACKTRACE_KEY = "stacktrace"
    private const val EXTRAS_KEY = "extras"

    const val SOM_TAG = "SOM_ERROR"

    object SomMessage {

        //Confirm Shipping
        const val CONFIRM_SHIPPING_ERROR = "som confirm shipping error"
        const val CHANGE_COURIER_ERROR = "som change courier error"
        const val GET_COURIER_LIST_ERROR = "som get courier list error"

        //Request pickup
        const val GET_REQUEST_PICKUP_DATA_ERROR = "som get request pickup data error"
        const val REQUEST_PICKUP_ERROR = "som request pickup error"

    }

    fun logExceptionToCrashlytics(throwable: Throwable, message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                val exceptionMessage = "$message - ${throwable.localizedMessage}"
                FirebaseCrashlytics.getInstance().recordException(
                    LogisticSellerException(
                        message = exceptionMessage, cause = throwable
                    )
                )
            } else {
                throwable.printStackTrace()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun getErrorMessage(throwable: Throwable, context: Context): String {
        return if (throwable is UnknownHostException || throwable is SocketTimeoutException) {
            context.getString(R.string.error_message_no_internet_connection)
        } else {
            throwable.message ?: context.getString(R.string.error_message_server_fault)
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
