package com.tokopedia.home.util

import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import okhttp3.internal.http2.ConnectionShutdownException
import org.apache.commons.lang3.exception.ExceptionUtils
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException

object HomeServerLogger {

    private const val HOME_STATUS_ERROR_TAG = "HOME_STATUS"

    const val TYPE_CANCELLED_INIT_FLOW = "revamp_cancelled_init_flow"
    const val TYPE_REVAMP_ERROR_INIT_FLOW = "revamp_error_init_flow"
    const val TYPE_REVAMP_ERROR_REFRESH = "revamp_error_refresh"
    const val TYPE_REVAMP_EMPTY_UPDATE = "revamp_empty_update"

    const val TYPE_WALLET_APP_ERROR = "wallet_app_error"
    const val TYPE_WALLET_BALANCE_EMPTY = "wallet_balance_empty"
    const val TYPE_WALLET_POINTS_EMPTY = "wallet_points_empty"

    const val TYPE_RECOM_SET_ADAPTER_ERROR = "recom_set_adapter_error"

    const val TYPE_ERROR_SUBMIT_WALLET = "wallet_app_error_submit"
    const val TYPE_ERROR_ON_LAYOUT_CHILDREN_BALANCE = "error_on_layout_children_balance_adapter"

    fun logWarning(
        type: String?,
        throwable: Throwable?,
        reason: String = "",
        data: String = ""
    ) {
        if (type == null || throwable == null || isExceptionExcluded(throwable)) return

        timberLogWarning(type, ExceptionUtils.getStackTrace(throwable), reason, data)
    }

    private fun isExceptionExcluded(throwable: Throwable): Boolean {
        if (throwable is UnknownHostException) return true
        if (throwable is SocketException) return true
        if (throwable is InterruptedIOException) return true
        if (throwable is ConnectionShutdownException) return true

        return false
    }

    private fun timberLogWarning(type: String, stackTrace: String, reason: String, data: String) {
        ServerLogger.log(Priority.P2, HOME_STATUS_ERROR_TAG, mapOf(
            "type" to type,
            "error" to stackTrace,
            "reason" to reason,
            "data" to data
        ))
    }
}