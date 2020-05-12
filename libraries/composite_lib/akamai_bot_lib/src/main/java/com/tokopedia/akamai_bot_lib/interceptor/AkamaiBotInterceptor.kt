package com.tokopedia.akamai_bot_lib.interceptor

import android.content.Context
import com.akamai.botman.CYFMonitor
import com.tokopedia.akamai_bot_lib.*
import com.tokopedia.network.exception.MessageErrorException
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
                { System.currentTimeMillis() },
                { context.getExpiredTime() },
                { time -> context.setExpiredTime(time) },
                { context.setAkamaiValue(CYFMonitor.getSensorData()) },
                { context.getAkamaiValue() }
        )

        newRequest.addHeader("X-acf-sensor-data",
                if (akamaiValue.isNotEmpty()) akamaiValue else "")

        val response = chain.proceed(newRequest.build())

        if (response.code() == ERROR_CODE && response.header(HEADER_AKAMAI_KEY)?.contains(HEADER_AKAMAI_VALUE, true) == true) {
            throw MessageErrorException(ERROR_MESSAGE_AKAMAI)
        }
        return response
    }
}