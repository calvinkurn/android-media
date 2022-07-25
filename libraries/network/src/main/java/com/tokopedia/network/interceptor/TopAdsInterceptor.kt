package com.tokopedia.network.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class TopAdsInterceptor constructor(val context: Context) : Interceptor {

    private val sp = context.getSharedPreferences(TOP_ADS_SHARED_PREF_KEY, Context.MODE_PRIVATE)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var newRequest: Request.Builder = chain.request().newBuilder()
        newRequest = addDebugHeader(newRequest)
        return chain.proceed(newRequest.build())
    }

    private fun addDebugHeader(newRequest: Request.Builder): Request.Builder {

        val newHeader = sp.getString(RESPONSE_HEADER_KEY, "")
        if (!newHeader.isNullOrEmpty()) newRequest.addHeader(TOP_ADS_TRACKING_KEY, newHeader)
        return newRequest
    }

    companion object {
        const val RESPONSE_HEADER_KEY = "Tkp-Enc-Sessionid"
        const val TOP_ADS_SHARED_PREF_KEY = "TopAdsSharedPreference"
        const val TOP_ADS_TRACKING_KEY = "Tkpd-Tracking-ID"
    }
}

