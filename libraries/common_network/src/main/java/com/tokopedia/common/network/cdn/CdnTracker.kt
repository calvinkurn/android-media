package com.tokopedia.common.network.cdn

import android.content.Context
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import okhttp3.Response
import kotlin.coroutines.CoroutineContext

internal object CdnTracker : CoroutineScope {

    private const val TAG_ANALYTIC = "DEV_CDN_MONITORING_STATIC"
    private const val PAGE_NAME_NOT_FOUND = "None"
    private const val CONTENT_LENGTH = "Content-Length"
    private const val CONTENT_TYPE = "Content-Type"
    private const val X_CACHE = "X-Cache"
    private const val X_TKPD_CDN_NAME = "x-tkpd-cdn-name"

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    fun Response.mapping(context: Context): MutableMap<String, String> {
        val responseTime = receivedResponseAtMillis - sentRequestAtMillis
        val contentLength = headers[CONTENT_LENGTH].toString()
        val popIp = headers[X_CACHE].toString()
        val cdnName = headers[X_TKPD_CDN_NAME].toString()
        val contentType = headers[CONTENT_TYPE].toString()
        return mutableMapOf(
            "page" to context.pageName(),
            "url" to request.url.toUrl().toString(),
            "cdn_name" to cdnName,
            "size" to contentLength,
            "response_time" to responseTime.toString(),
            "response_code" to code.toString(),
            "pop_ip" to popIp,
            "content_type" to contentType
        )
    }

    @JvmStatic
    fun log(
        context: Context,
        response: Response
    ) {
        ServerLogger.log(
            priority = Priority.P1,
            tag = TAG_ANALYTIC,
            message = response.mapping(context).apply {
                put("error_description", response.message)
            }
        )
    }

    fun Context.pageName(): String {
        return try {
            javaClass.name.split(".").last()
        } catch (ignored: Throwable) {
            PAGE_NAME_NOT_FOUND
        }
    }
}
