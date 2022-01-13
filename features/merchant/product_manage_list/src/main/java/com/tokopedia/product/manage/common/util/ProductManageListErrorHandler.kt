package com.tokopedia.product.manage.common.util

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.product.manage.BuildConfig
import java.lang.RuntimeException

object ProductManageListErrorHandler {

    // Scalyr/New Relic Error Keys
    private const val ERROR_TYPE_KEY = "error_type"
    private const val DEVICE_ID_KEY = "device_id"
    private const val MESSAGE_KEY = "message"
    private const val STACKTRACE_KEY = "stacktrace"
    private const val EXTRAS_KEY = "extras"

    const val PRODUCT_MANAGE_TAG = "PRODUCT_MANAGE_ERROR"

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

    fun logExceptionToCrashlytics(t: Throwable) {
        try {
            FirebaseCrashlytics.getInstance().recordException(t)
        }catch (e: IllegalStateException) {
            e.printStackTrace()
        }
    }

    fun logExceptionToCrashlytics(throwable: Throwable, message: String) {
        try {
            if (!BuildConfig.DEBUG) {
                val exceptionMessage = "$message - ${throwable.localizedMessage}"
                FirebaseCrashlytics.getInstance().recordException(RuntimeException(
                    exceptionMessage,
                    throwable
                ))
            } else {
                throwable.printStackTrace()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace()
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

    fun getSellerOutageErrorMessageMap(
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