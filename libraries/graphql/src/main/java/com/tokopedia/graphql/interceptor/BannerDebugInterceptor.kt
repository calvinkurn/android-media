package com.tokopedia.graphql.interceptor

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.config.GlobalConfig
import okhttp3.Interceptor
import okhttp3.Response

class BannerEnvironmentInterceptor(context: Context) : Interceptor {

    var sharedPreferences: SharedPreferences = context.getSharedPreferences(BETA_INTERCEPTOR_PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        const val URL_BETA = "1"
        const val HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "x-tkpd-beta"
        const val BETA_INTERCEPTOR_PREF_NAME = "BetaInterceptor_banner_debug"
        const val IS_BETA_TOKOPEDIA = "IS_BETA_TOKOPEDIA_BANNER_DEBUG"

        @JvmStatic
        fun isBeta(context: Context): Boolean {
            val sharedPreferences = context
                .getSharedPreferences(BETA_INTERCEPTOR_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(IS_BETA_TOKOPEDIA, false)
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response = chain.proceed(request)

        fun saveBeta(isBeta: Boolean) {
            val edit = sharedPreferences.edit()
            edit.putBoolean(IS_BETA_TOKOPEDIA, isBeta)
            edit.apply()
        }

        val headers = response.headers
        if (headers.size > 0) {
            when (GlobalConfig.APPLICATION_TYPE) {
                GlobalConfig.CONSUMER_APPLICATION, GlobalConfig.SELLER_APPLICATION -> {
                    val get = headers.get(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                    if (get.equals(URL_BETA)) {
                        saveBeta(true)
                    } else {
                        saveBeta(false)
                    }
                }
            }
        }

        return response
    }
}
