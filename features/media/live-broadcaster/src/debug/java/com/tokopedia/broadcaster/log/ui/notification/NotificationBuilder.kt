package com.tokopedia.broadcaster.log.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.tokopedia.broadcaster.R
import com.tokopedia.broadcaster.log.ui.activity.NetworkLogActivity

object NotificationBuilder {

    private const val NOTIFICATION_ID = 990
    private const val CHANNEL_NAME = "Live Broadcaster Chucker"
    private const val CHANNEL_ID = "broadcaster_chucker"

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun build(context: Context, content: String) {
        createNotificationChannel(context)

        val intent = Intent(context, NetworkLogActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        } else {
            PendingIntent.getActivity(context, 0, intent, 0)
        }
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("(Debug) - Live Broadcaster")
            .setContentText(content)
            .setSmallIcon(R.drawable.live_broadcaster_ic_notificarion_icon)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(false)
            .setOngoing(false)

        with(NotificationManagerCompat.from(context)) {
            notify(NOTIFICATION_ID, builder.build())
        }
    }

}
