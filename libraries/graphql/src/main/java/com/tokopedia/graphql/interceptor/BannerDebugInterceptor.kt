package com.tokopedia.graphql.interceptor

import android.content.Context
import android.content.SharedPreferences
import com.tokopedia.config.GlobalConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class BannerDebugInterceptor(private val context: Context) : Interceptor {

    var sharedPreferences: SharedPreferences = context
        .getSharedPreferences(BETA_INTERCEPTOR_PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        val URL_BETA = "1"
        val HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "x-tkpd-beta"
        val BETA_INTERCEPTOR_PREF_NAME = "BetaInterceptor_banner_debug"
        val IS_BETA_TOKOPEDIA = "IS_BETA_TOKOPEDIA_BANNER_DEBUG"

        @JvmStatic
        fun isBeta(context: Context): Boolean {
            val sharedPreferences = context
                .getSharedPreferences(BETA_INTERCEPTOR_PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(IS_BETA_TOKOPEDIA, false)
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: IOException) {
            throw e
        }

        fun saveBeta(isBeta: Boolean) {
            val sharedPreferences = context
                .getSharedPreferences(BETA_INTERCEPTOR_PREF_NAME, Context.MODE_PRIVATE)
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
//                GlobalConfig.CONSUMER_PRO_APPLICATION -> {
//                    val get = headers.get(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
//                    createNotif(
//                        context,
//                        if (get.equals(URL_BETA)) {
//                            appName + "-beta"
//                        } else {
//                            appName
//                        }
//                    ) {
//                        saveBeta(true)
//                    }
//                }
                else -> {
                }
            }
        }

        return response
    }
}
