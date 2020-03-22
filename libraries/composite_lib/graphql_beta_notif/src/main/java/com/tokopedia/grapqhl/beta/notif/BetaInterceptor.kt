package com.tokopedia.grapqhl.beta.notif

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.tokopedia.graphql.beta.notif.R
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


const val CHANNEL_ID = "beta"
const val NOTIFICATION_ID = 123 shr 5

class BetaInterceptor(private val context: Context) : Interceptor {

    lateinit var sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context
                .getSharedPreferences(BETA_INTERCEPTOR_PREF_NAME, Context.MODE_PRIVATE)
    }

    companion object {
        val URL_BETA = "1"
        val HEADER_ACCESS_CONTROL_ALLOW_ORIGIN = "x-tkpd-beta"
        val BETA_INTERCEPTOR_PREF_NAME = "BetaInterceptor"
        val IS_BETA_TOKOPEDIA = "IS_BETA_TOKOPEDIA"
        
        @JvmStatic
        fun isBeta(context: Context) : Boolean{
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
        
        val saveBeta = fun(context: Context, isBeta: Boolean) {
            val sharedPreferences = context
                    .getSharedPreferences(BETA_INTERCEPTOR_PREF_NAME, Context.MODE_PRIVATE)
            val edit = sharedPreferences.edit()
            edit.putBoolean(IS_BETA_TOKOPEDIA, isBeta)
            edit.apply()
        }

        val headers = response.headers()
        if (headers.size() > 0) {

            var mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationManager.createNotificationChannel(
                        NotificationChannel(CHANNEL_ID,
                                context.getString(R.string.beta_notification_category),
                                NotificationManager.IMPORTANCE_LOW))
            }

            var get = headers.get(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
            if (get.equals(URL_BETA)) {
                context.let {
                    saveBeta(it, true)
                    
                    val remoteView = RemoteViews(context.packageName, R.layout.notification_layout)

                    val mBuilder =
                            NotificationCompat.Builder(context, CHANNEL_ID)
                                    .setSmallIcon(R.drawable.beta_icon)
                                    .setCustomContentView(remoteView)
                                    .setColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))

                    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build())
                }
            } else {
                saveBeta(context, false)
                mNotificationManager.cancel(NOTIFICATION_ID)
            }
        }

        return response
    }
}