package com.tokopedia.grapqhl.beta.notif

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.tokopedia.graphql.beta.notif.R
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class BetaInterceptor(private val context: Context) : Interceptor {


    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: IOException) {
            throw e
        }

        val headers = response.headers()
        if (headers.size() > 0) {
            var get = headers.get("access-control-allow-origin")
            if (get.equals("https://gql-beta.tokopedia.com")) {
                context.let {
                    //Get an instance of NotificationManager//

                    val mBuilder =
                            NotificationCompat.Builder(context, "beta")
                                    .setSmallIcon(R.drawable.beta_icon)
                                    .setContentTitle("My notification")
                                    .setContentText("Hello World!")


                    // Gets an instance of the NotificationManager service//

                    var mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        mNotificationManager.createNotificationChannel(
                                NotificationChannel("beta",
                                        context.getString(R.string.chucker_notification_category),
                                        NotificationManager.IMPORTANCE_LOW))
                    }

                    mNotificationManager.notify(1, mBuilder.build())
                }
            }
        }
        return response
    }
}