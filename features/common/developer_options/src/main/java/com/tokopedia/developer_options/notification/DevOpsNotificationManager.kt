package com.tokopedia.developer_options.notification

import android.app.Application
import android.app.NotificationChannel
import android.util.Log
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


/**
 * Created By : Jonathan Darwin on September 28, 2022
 */
class DevOpsNotificationManager(
    private val application: Application
) {
    private val notificationManager = NotificationManagerCompat.from(application)

    fun start() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
            fun onAppBackgrounded() {
                Log.d("MyApp", "App in background")
                dismissNotification()
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_START)
            fun onAppForegrounded() {
                Log.d("MyApp", "App in foreground")
                showNotification()
            }
        })
    }

    private fun showNotification() {
        val intent = RouteManager.getIntent(application, ApplinkConst.DEVELOPER_OPTIONS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(application, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val builder = NotificationCompat.Builder(application, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_developer_mode)
            .setContentTitle("Tokopedia")
            .setContentText("Click here to open Developer Options")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = NAME
            val descriptionText = DESCRIPTION
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, builder)
    }

    private fun dismissNotification() {
        val notificationManager = NotificationManagerCompat.from(application)
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private companion object {
        const val NOTIFICATION_ID = 123213
        const val NAME = "Tokopedia Developer Options"
        const val DESCRIPTION = "Tokopedia Developer Options"
        const val CHANNEL_ID = "DEV_OPS"
    }
}
