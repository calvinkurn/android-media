package com.tokopedia.grapqhl.beta.notif

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.tokopedia.config.GlobalConfig
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

        val saveBeta = fun(context: Context, isBeta: Boolean) {
            val sharedPreferences = context
                    .getSharedPreferences(BETA_INTERCEPTOR_PREF_NAME, Context.MODE_PRIVATE)
            val edit = sharedPreferences.edit()
            edit.putBoolean(IS_BETA_TOKOPEDIA, isBeta)
            edit.apply()
        }

        val headers = response.headers()
        if (headers.size() > 0) {
            val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mNotificationManager.createNotificationChannel(
                        NotificationChannel(CHANNEL_ID,
                                context.getString(R.string.beta_notification_category),
                                NotificationManager.IMPORTANCE_LOW))
            }

            determineShowNotif(GlobalConfig.APPLICATION_TYPE, context) { appName: String, appType: Int, context: Context ->

                val createNotif = { context: Context, appName: String, function: (context: Context) -> Unit ->
                    {
                        context.let {
                            function(context)
                            val remoteView = RemoteViews(context.packageName, R.layout.notification_layout)
                            remoteView.setTextViewText(R.id.mynotifyexpnd, appName)
                            val mBuilder =
                                    NotificationCompat.Builder(context, CHANNEL_ID)
                                            .setSmallIcon(R.drawable.beta_icon)
                                            .setCustomContentView(remoteView)
                                            .setColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))

                            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build())
                        }
                    }
                }

                val cancelNotif = { function: (context: Context) -> Unit ->
                    {
                        function(context)
                        mNotificationManager.cancel(NOTIFICATION_ID)
                    }
                }

                when (GlobalConfig.APPLICATION_TYPE) {
                    GlobalConfig.CONSUMER_APPLICATION, GlobalConfig.SELLER_APPLICATION -> {
                        val get = headers.get(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                        if (get.equals(URL_BETA)) {
                            createNotif(context, appName) {
                                saveBeta(it, true)
                            }
                        } else {
                            cancelNotif {
                                saveBeta(it, false)
                            }
                        }
                    }
                    GlobalConfig.CONSUMER_PRO_APPLICATION -> {
                        val get = headers.get(HEADER_ACCESS_CONTROL_ALLOW_ORIGIN)
                        createNotif(context, if (get.equals(URL_BETA)) {
                            appName + "-beta"
                        } else {
                            appName
                        }) {
                            saveBeta(it, true)
                        }
                    }
                    else -> {
                    }
                }

            }
        }

        return response
    }

    fun determineShowNotif(appType: Int = GlobalConfig.CONSUMER_APPLICATION, context: Context,
                           function: (appName: String, appType: Int, context: Context) -> Unit) {

        // determine string to present
        var appName = ""
        when (GlobalConfig.APPLICATION_TYPE) {
            GlobalConfig.CONSUMER_APPLICATION ->
                appName = context.getString(R.string.tokopedia_beta)
            GlobalConfig.SELLER_APPLICATION ->
                appName = "sellerapp"
            GlobalConfig.CONSUMER_PRO_APPLICATION ->
                appName = "Tokopedia Pro"
        }

        function(appName, appType, context)
    }
}