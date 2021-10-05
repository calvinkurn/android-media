package com.tokopedia.tkpd

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.tokopedia.config.GlobalConfig

const val CHANNEL_ID = "beta"
const val NOTIFICATION_ID = 123 shr 5

fun showBanner(context: Context) {
    val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        mNotificationManager.createNotificationChannel(
            NotificationChannel(CHANNEL_ID,
                context.getString(R.string.alpha_notification_category),
                NotificationManager.IMPORTANCE_LOW)
        )
    }

    determineShowNotif(GlobalConfig.APPLICATION_TYPE, context) { appName: String, appType: Int, context: Context ->

        val createNotif = { context: Context, appName: String ->
            {
                context.let {
                    val remoteView = RemoteViews(context.packageName, R.layout.alpha_notification_layout)
                    remoteView.setTextViewText(R.id.mynotifyexpnd, appName)
                    val mBuilder =
                        NotificationCompat.Builder(context, CHANNEL_ID)
                            .setCustomContentView(remoteView)
                            .setColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))

                    mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build())
                }
            }
        }
        createNotif(context, appName)
    }
}

fun determineShowNotif(appType: Int = GlobalConfig.CONSUMER_APPLICATION, context: Context,
                       function: (appName: String, appType: Int, context: Context) -> Unit) {

    // determine string to present
    var appName = "Tokopedia Alpha"


    function(appName, appType, context)
}