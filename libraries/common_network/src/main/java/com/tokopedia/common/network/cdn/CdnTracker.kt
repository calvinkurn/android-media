package com.tokopedia.common.network.cdn

import android.content.Context
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

internal object CdnTracker : CoroutineScope {

    private const val TAG_ANALYTIC = "DEV_CDN_MONITORING_STATIC"

    private const val PAGE_NAME_NOT_FOUND = "None"

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    fun succeed(
        context: Context,
        url: String,
        cdnName: String,
        responseTime: String,
        responseCode: String,
        responseSize: String,
        message: String,
        popIp: String
    ) {
        val pageName = context.pageName()

        // tracker
        ServerLogger.log(
            priority = Priority.P1,
            tag = TAG_ANALYTIC,
            message = mapOf(
                "page" to pageName,
                "url" to url,
                "cdn_name" to cdnName,
                "size" to responseSize,
                "response_time" to responseTime,
                "response_code" to responseCode,
                "pop_ip" to popIp,
                "error_description" to message
            )
        )
    }

    fun failed(
        context: Context,
        url: String,
        cdnName: String,
        responseTime: String,
        responseCode: String,
        popIp: String,
        exception: Throwable
    ) {
        val pageName = context.pageName()

        ServerLogger.log(
            priority = Priority.P1,
            tag = TAG_ANALYTIC,
            message = mapOf(
                "page" to pageName,
                "url" to url,
                "cdn_name" to cdnName,
                "size" to "n/a",
                "response_time" to responseTime,
                "response_code" to responseCode,
                "pop_ip" to popIp,
                "error_description" to exception.localizedMessage
            )
        )
    }

    fun Context.pageName(): String {
        return try {
            javaClass.name.split(".").last()
        } catch (ignored: Throwable) {
            PAGE_NAME_NOT_FOUND
        }
    }

    private fun getQualitySetting(index: Int): String {
        return when (index) {
            0 -> "Automatic"
            1 -> "Low"
            2 -> "High"
            else -> "Unknown"
        }
    }
}
