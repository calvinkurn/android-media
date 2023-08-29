package com.tokopedia.common.network.cdn

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class CdnInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val responseTime = response.receivedResponseAtMillis - response.sentRequestAtMillis
        val contentLength = response.headers.get("Content-Length").toString()
        val popIp = response.headers.get("X-Cache").toString()
        val cdnName = response.headers.get("x-tkpd-cdn-name").toString()
        val contentType = response.headers.get("Content-Type").toString()
        try {
            CdnTracker.succeed(
                context = context,
                url = response.request.url.toUrl().toString(),
                cdnName = cdnName,
                responseTime = responseTime.toString(),
                responseCode = response.code.toString(),
                responseSize = contentLength,
                popIp = popIp,
                contentType = contentType,
                message = response.message
            )
        } catch (e: Exception) {
            CdnTracker.failed(
                context = context,
                url = response.request.url.toUrl().toString(),
                cdnName = cdnName,
                responseTime = responseTime.toString(),
                responseCode = response.code.toString(),
                popIp = popIp,
                contentType = contentType,
                exception = e
            )
        }
        return response
    }
}
