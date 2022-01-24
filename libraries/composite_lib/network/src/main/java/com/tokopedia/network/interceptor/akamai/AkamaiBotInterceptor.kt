package com.tokopedia.network.interceptor.akamai

import android.content.Context
import com.akamai.botman.CYFMonitor
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/*
   * 21 August 2020
   * Duplicated from Akamai Bot Interceptor Library for composite network lib to accommodate Refresh Token usecase only
 */

class AkamaiBotInterceptor(val context: Context) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        CYFMonitor.setLogLevel(CYFMonitor.INFO)

        val newRequest: Request.Builder = chain.request().newBuilder()

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

        if (response.code == ERROR_CODE && response.header(HEADER_AKAMAI_KEY)?.contains(HEADER_AKAMAI_VALUE, true) == true) {
            throw AkamaiErrorException(ERROR_MESSAGE_AKAMAI)
        }
        return response
    }
}