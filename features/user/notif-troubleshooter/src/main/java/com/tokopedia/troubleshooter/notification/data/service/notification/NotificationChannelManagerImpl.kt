package com.tokopedia.troubleshooter.notification.data.service.notification

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.content.Context.NOTIFICATION_SERVICE

class NotificationChannelManagerImpl(
        val context: Context
) : NotificationChannelManager {

    override fun notificationChannel(): Int {
        val manager = context.getSystemService(NOTIFICATION_SERVICE) as? NotificationManager?
        if (!hasNotificationChannel()) return NO_CHANNEL
        return manager?.getNotificationChannel(CHANNEL_ID)?.importance?: NO_CHANNEL
    }

    override fun hasNotificationChannel(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    override fun isImportanceChannel(): Boolean {
        val importance = notificationChannel()
        return if (hasNotificationChannel()) {
            importance == NotificationManager.IMPORTANCE_HIGH
                    || importance == NotificationManager.IMPORTANCE_DEFAULT
        } else {
            false
        }
    }

    override fun isNotificationChannelEnabled(): Boolean {
        return if (hasNotificationChannel()) {
            notificationChannel() == NotificationManager.IMPORTANCE_NONE
        } else {
            false
        }
    }

    companion object {
        private const val CHANNEL_ID = "ANDROID_GENERAL_CHANNEL"
        private const val NO_CHANNEL = -1
    }

}