package com.tokopedia.fakeresponse

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

object NotificationHelper {
    private val NOTIF_CHANNEL_ID = "FAKE_RESPONSE_CHANNEL"
    private val NOTIF_CHANNEL_NAME = "FAKE RESPONSE"
    private val NOTIF_TITLE_FAKERESPONSE_DEBUGGER = "Open FakeResponse Debugger"
    private val NOTIF_ID_FAKERESPONSE_DEBUGGER = 89327

    fun show(context: Context, gqlQueryName: String, intent: Intent) {
        showNotif(context, contentText = gqlQueryName, intent)
    }

    private fun showNotif(
        context: Context,
        contentText: String?,
        intent: Intent
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        setNotificationChannel(notificationManager)

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        val builder = NotificationCompat.Builder(context, NOTIF_CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setContentTitle(NOTIF_TITLE_FAKERESPONSE_DEBUGGER)
            .setContentText(contentText)
            .setAutoCancel(true)
            .setShowWhen(true)
            .setSmallIcon(R.drawable.ic_fake_notif)
            .setPriority(NotificationCompat.PRIORITY_LOW)
        notificationManager.notify(NOTIF_ID_FAKERESPONSE_DEBUGGER, builder.build())
    }

    private fun setNotificationChannel(notificationManager: NotificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(
                NotificationChannel(
                    NOTIF_CHANNEL_ID,
                    NOTIF_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW
                )
            )
        }
    }
}