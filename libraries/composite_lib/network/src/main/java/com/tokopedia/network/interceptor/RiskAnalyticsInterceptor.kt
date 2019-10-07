package com.tokopedia.network.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @author okasurya on 2019-07-31.
 */
class RiskAnalyticsInterceptor(context: Context) : Interceptor {
    val CASHSHIELD_SESSION_ID = "Cshld-SessionID"
    val CASHSHIELD = "CASHSHIELD"
    val SESSION = "session"
    var sharedPref = context.getSharedPreferences(CASHSHIELD, Context.MODE_PRIVATE)

    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
        newRequest.removeHeader(CASHSHIELD_SESSION_ID)
        newRequest.addHeader(CASHSHIELD_SESSION_ID, sharedPref.getString(SESSION, "")?: "")
        return chain.proceed(newRequest.build())
    }
}