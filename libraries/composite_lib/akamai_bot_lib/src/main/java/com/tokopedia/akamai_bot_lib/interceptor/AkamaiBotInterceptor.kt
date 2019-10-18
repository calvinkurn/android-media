package com.tokopedia.akamai_bot_lib.interceptor

import com.akamai.botman.CYFMonitor
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AkamaiBotInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        CYFMonitor.setLogLevel(CYFMonitor.INFO)

        var newRequest: Request.Builder = chain.request().newBuilder()
        newRequest.addHeader("X-acf-sensor-data", CYFMonitor.getSensorData() ?: "")

        return chain.proceed(newRequest.build())
    }
}