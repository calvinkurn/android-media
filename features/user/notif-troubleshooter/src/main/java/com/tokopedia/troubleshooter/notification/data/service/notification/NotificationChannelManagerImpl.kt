package com.tokopedia.troubleshooter.notification.data.service.notification

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class NotificationChannelManagerImpl(
        val context: Context
) : NotificationChannelManager {

    @SuppressLint("NewApi")
    override fun getNotificationChannel(): Int? {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager?
        return manager?.getNotificationChannel(CHANNEL_ID)?.importance?: -1
    }

    override fun hasNotificationChannel(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
    }

    override fun isImportanceChannel(): Boolean {
        val importance = getNotificationChannel()
        return if (hasNotificationChannel()) {
            importance == NotificationManager.IMPORTANCE_HIGH
                    || importance == NotificationManager.IMPORTANCE_DEFAULT
        } else {
            false
        }
    }

    override fun isNotificationChannelEnabled(): Boolean {
        return if (hasNotificationChannel()) {
            getNotificationChannel() == NotificationManager.IMPORTANCE_NONE
        } else {
            false
        }
    }

    companion object {
        private const val CHANNEL_ID = "ANDROID_GENERAL_CHANNEL"
    }

}