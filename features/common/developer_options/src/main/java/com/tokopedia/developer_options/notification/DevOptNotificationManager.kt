package com.tokopedia.developer_options.notification

import android.app.Application
import android.app.NotificationChannel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.core.app.NotificationManagerCompat
import android.app.NotificationManager
import android.os.Build
import android.app.PendingIntent
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.developer_options.R
import com.tokopedia.developer_options.config.DevOptConfig


/**
 * Created By : Jonathan Darwin on September 28, 2022
 */
class DevOptNotificationManager(
    private val application: Application
) {
    private val notificationManager = NotificationManagerCompat.from(application)

    fun start() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onAppForegrounded() {
                showNotificationIfEnabled()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onAppBackgrounded() {
                dismissNotification()
            }
        })
    }

    fun showNotificationIfEnabled() {
        if(DevOptConfig.isDevOptOnNotifEnabled(application)) {
            val intent = RouteManager.getIntent(application, ApplinkConst.DEVELOPER_OPTIONS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                PendingIntent.getActivity(application, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            } else {
                PendingIntent.getActivity(application, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
            }
            val builder = NotificationCompat.Builder(application, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_developer_mode)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(NOTIFICATION_DESCRIPTION)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = CHANNEL_NAME
                val descriptionText = CHANNEL_DESCRIPTION
                val importance = NotificationManager.IMPORTANCE_LOW
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(NOTIFICATION_ID, builder)
        }
    }

    fun dismissNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private companion object {
        const val NOTIFICATION_ID = 123213

        const val NOTIFICATION_TITLE = "Tokopedia"
        const val NOTIFICATION_DESCRIPTION = "Click here to open Developer Options"

        const val CHANNEL_NAME = "Tokopedia Developer Options"
        const val CHANNEL_DESCRIPTION = "Tokopedia Developer Options"
        const val CHANNEL_ID = "DEV_OPS"
    }
}
