package com.tokopedia.akamai_bot_lib.interceptor

import android.content.Context
import android.text.TextUtils
import com.akamai.botman.CYFMonitor
import com.tokopedia.akamai_bot_lib.*
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class AkamaiBotInterceptor(val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        CYFMonitor.setLogLevel(CYFMonitor.INFO)

        var newRequest: Request.Builder = chain.request().newBuilder()

        val akamaiValue = setExpire(
                { System.currentTimeMillis() / 1000 },
                { context.getExpiredTime() },
                { time -> context.setExpiredTime(time) },
                { context.setAkamaiValue(CYFMonitor.getSensorData()) },
                { context.getAkamaiValue() }
        )

        newRequest.addHeader("X-acf-sensor-data",
                if (TextUtils.isEmpty(akamaiValue)) akamaiValue else "")

        return chain.proceed(newRequest.build())
    }
}