package com.tokopedia.network.interceptor

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody

/**
 * @author okasurya on 2019-07-31.
 */
class RiskAnalyticsInterceptor(context: Context) : Interceptor {
    val CASHSHIELD_SESSION_ID = "Cshld-SessionID"
    val CASHSHIELD = "CASHSHIELD"
    val SESSION = "session"
    var sharedPref = context.getSharedPreferences(CASHSHIELD, Context.MODE_PRIVATE)

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val newRequest = chain.request().newBuilder()
            newRequest.removeHeader(CASHSHIELD_SESSION_ID)
            newRequest.addHeader(CASHSHIELD_SESSION_ID, sharedPref.getString(SESSION, "") ?: "")
            return chain.proceed(newRequest.build())
        }catch (e : java.lang.Exception){
            return Response.Builder()
                    .code(418) //Whatever code
                    .body(ResponseBody.create(MediaType.get("text/html; charset=utf-8"), ""))
                    .protocol(Protocol.HTTP_2)
                    .message("There is an error. Please try again")
                    .request(chain.request())
                    .build()
        }
    }
}