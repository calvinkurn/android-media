package com.tokopedia.network.interceptor

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import okhttp3.Interceptor
import okhttp3.Response

class DeprecatedApiInterceptor(val context: Context) : Interceptor {
    val WARNING_HEADER_KEY = "warning"

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val warningHeaderVal = response.header(WARNING_HEADER_KEY)
        if (!warningHeaderVal.isNullOrBlank() && context.getIsEnableDeprecatedAPISwitcherToaster()) {
            val handler = Handler(Looper.getMainLooper())
            handler.post {
                Toast.makeText(context, warningHeaderVal, Toast.LENGTH_LONG).show()
            }
        }
        return response
    }

    private fun Context.getIsEnableDeprecatedAPISwitcherToaster(): Boolean {
        val sharePref = getSharedPreferences(
            DEPRECATED_API_SWITCHER_TOASTER_SP_NAME,
            MODE_PRIVATE
        )

        return sharePref.getBoolean(
            DEPRECATED_API_SWITCHER_TOASTER_KEY,
            false
        )
    }

    companion object {
        private const val DEPRECATED_API_SWITCHER_TOASTER_SP_NAME = "deprecated_switcher_toggle"
        private const val DEPRECATED_API_SWITCHER_TOASTER_KEY = "deprecated_switcher_key"
    }
}
