package com.tokopedia.network.interceptor

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import okhttp3.Interceptor
import okhttp3.Response

class
DeprecatedApiInterceptor(val context: Context) : Interceptor {
    val WARNING_HEADER_KEY = "warning"

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val warningHeaderVal = response.header(WARNING_HEADER_KEY)
        if (!warningHeaderVal.isNullOrBlank()) {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                Toast.makeText(context, warningHeaderVal, Toast.LENGTH_LONG).show()
            }
        }
        return response
    }
}
