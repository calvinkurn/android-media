package com.tokopedia.abstraction.base.view.debugbanner

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.tokopedia.config.GlobalConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

const val CHANNEL_ID = "beta"
const val NOTIFICATION_ID = 123 shr 5

//class BetaInterceptor(private val context: Context) : Interceptor {
//
//    var sharedPreferences: SharedPreferences = context
//        .getSharedPreferences(BETA_INTERCEPTOR_PREF_NAME, Context.MODE_PRIVATE)
//
//    companion object {
//        val URL_BETA = "1"
//        val HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "x-tkpd-beta"
//        val BETA_INTERCEPTOR_PREF_NAME = "BetaInterceptor"
//        val IS_BETA_TOKOPEDIA = "IS_BETA_TOKOPEDIA"
//
//        @JvmStatic
//        fun isBeta(context: Context): Boolean {
//            val sharedPreferences = context
//                .getSharedPreferences(BETA_INTERCEPTOR_PREF_NAME, Context.MODE_PRIVATE)
//            return sharedPreferences.getBoolean(IS_BETA_TOKOPEDIA, false)
//        }
//    }
//
//    override fun intercept(chain: Interceptor.Chain): Response {
//        val request = chain.request()
//        val response: Response
//        try {
//            response = chain.proceed(request)
//        } catch (e: IOException) {
//            throw e
//        }
//
//        fun saveBeta(isBeta: Boolean) {
//            val sharedPreferences = context
//                .getSharedPreferences(BETA_INTERCEPTOR_PREF_NAME, Context.MODE_PRIVATE)
//            val edit = sharedPreferences.edit()
//            edit.putBoolean(IS_BETA_TOKOPEDIA, isBeta)
//            edit.apply()
//        }
//
//        val headers = response.headers
//        if (headers.size > 0) {
//
//            when (GlobalConfig.APPLICATION_TYPE) {
//                GlobalConfig.CONSUMER_APPLICATION, GlobalConfig.SELLER_APPLICATION -> {
//                    val get = headers.get(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
//                    if (get.equals(URL_BETA)) {
//                        saveBeta(true)
//                    } else {
//                        saveBeta(false)
//                    }
//                }
////                GlobalConfig.CONSUMER_PRO_APPLICATION -> {
////                    val get = headers.get(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
////                    createNotif(
////                        context,
////                        if (get.equals(URL_BETA)) {
////                            appName + "-beta"
////                        } else {
////                            appName
////                        }
////                    ) {
////                        saveBeta(true)
////                    }
////                }
//                else -> {
//                }
//            }
//        }
//
//        return response
//    }
//
//}
