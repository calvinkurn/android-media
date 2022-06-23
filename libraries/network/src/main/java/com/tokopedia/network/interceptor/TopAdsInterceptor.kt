package com.tokopedia.network.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import kotlin.Throws
import java.io.IOException


class TopAdsInterceptor constructor(val context: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var newRequest: Request.Builder = chain.request().newBuilder()
        newRequest = addDebugHeader(newRequest)
        return chain.proceed(newRequest.build())
    }

    private fun addDebugHeader(newRequest: Request.Builder): Request.Builder {
        val sp = context.getSharedPreferences("x-tkp-srv-id", Context.MODE_PRIVATE)
        newRequest.addHeader("Tkp-Enc-Sessionid", sp.getString("myheader", "") ?: "")
        return newRequest
    }
}
