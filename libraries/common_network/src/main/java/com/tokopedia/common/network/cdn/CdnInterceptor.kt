package com.tokopedia.common.network.cdn

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody

class CdnInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val startTime = System.currentTimeMillis()
        val response = chain.proceed(chain.request())
        val responseTime = response.receivedResponseAtMillis - response.sentRequestAtMillis
        val contentLength = response.headers.get("Content-Length")
        val popIp = response.headers.get("X-Cache")
        val serverName = response.headers.get("Server")
        val contentType = response.headers.get("Content-Type")
        try {
            CdnTracker.succeed(
                context = context,
                url = response.request.url.toUrl().toString(),
                responseTime = responseTime.toString(),
                responseCode = response.code.toString(),
                responseSize = contentLength.toString(),
                popIp = popIp.toString(),
                message = response.message
            )
        } catch (e: Exception){
            CdnTracker.failed(
                context = context,
                url = response.request.url.toUrl().toString(),
                responseTime = responseTime.toString(),
                responseCode = response.code.toString(),
                popIp = popIp.toString(),
                exception = e
            )
        }
        return response
    }
}
