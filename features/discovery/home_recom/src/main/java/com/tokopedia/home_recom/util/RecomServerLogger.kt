package com.tokopedia.home_recom.util

import android.util.Log
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import okhttp3.internal.http2.ConnectionShutdownException
import org.apache.commons.lang3.exception.ExceptionUtils
import java.io.InterruptedIOException
import java.net.SocketException
import java.net.UnknownHostException

object RecomServerLogger {

    private const val RECOM_INFINITE_TOKONOW_ERROR_TAG = "RECOM_INFINITE_TOKONOW_STATUS"

    const val TYPE_ERROR_GET_MINICART = "recom_infinite_error_minicart"

    private const val TOPADS_RECOM_PAGE = "TOPADS_RECOM_PAGE"

    private const val MAX_LIMIT = 200
    const val TOPADS_RECOM_PAGE_TIMEOUT_EXCEEDED = "topads_recom_page_timeout"
    const val TOPADS_RECOM_PAGE_HIT_DYNAMIC_SLOTTING = "topads_recom_page_hit_dynamic_slotting"
    const val TOPADS_RECOM_PAGE_HIT_ADS_TRACKER = "topads_recom_page_hit_ads_tracker"
    const val TOPADS_RECOM_PAGE_HIT_ADS_TRACKER_IMPRESSION = "topads_recom_page_hit_ads_tracker_impression"
    const val TOPADS_RECOM_PAGE_IS_NOT_ADS = "topads_recom_page_is_not_ads"
    const val TOPADS_RECOM_PAGE_BE_ERROR = "topads_recom_page_be_error"
    const val TOPADS_RECOM_PAGE_GENERAL_ERROR = "topads_recom_page_general_error"

    fun logWarning(
            type: String?,
            throwable: Throwable?,
            reason: String = "",
            data: String = ""
    ) {
        if (type == null || throwable == null || isExceptionExcluded(throwable)) return

        timberLogWarning(type, ExceptionUtils.getStackTrace(throwable), reason, data)
    }

    fun logServer(
        tag: String,
        reason: String = "",
        productId: String = "-1",
        queryParam: String = "-1",
        data: String = "",
        throwable: Throwable? = null
    ) {
        var reasonValue = reason
        var dataValue = data

        throwable?.let {
            reasonValue = (it.message ?: "").take(MAX_LIMIT)
            dataValue = Log.getStackTraceString(it).take(MAX_LIMIT)
        }

        ServerLogger.log(
            Priority.P2,
            TOPADS_RECOM_PAGE,
            mapOf(
                "action" to tag,
                "productId" to productId,
                "queryParam" to queryParam.take(MAX_LIMIT),
                "reason" to reasonValue,
                "data" to dataValue
            )
        )
    }

    private fun isExceptionExcluded(throwable: Throwable): Boolean {
        if (throwable is UnknownHostException) return true
        if (throwable is SocketException) return true
        if (throwable is InterruptedIOException) return true
        if (throwable is ConnectionShutdownException) return true

        return false
    }

    private fun timberLogWarning(type: String, stackTrace: String, reason: String, data: String) {
        ServerLogger.log(Priority.P2, RECOM_INFINITE_TOKONOW_ERROR_TAG, mapOf(
                "type" to type,
                "error" to stackTrace,
                "reason" to reason,
                "data" to data
        ))
    }
}