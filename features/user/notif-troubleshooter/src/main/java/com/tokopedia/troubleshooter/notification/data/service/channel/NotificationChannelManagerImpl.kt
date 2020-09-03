package com.tokopedia.troubleshooter.notification.data.service.channel

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class NotificationChannelManagerImpl(
        val context: Context
) : NotificationChannelManager {

    @SuppressLint("NewApi")
    override fun getNotificationChannel(): Int {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        return manager.getNotificationChannel(CHANNEL_ID).importance
    }

    override fun hasNotificationChannel(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    companion object {
        private const val CHANNEL_ID = "ANDROID_GENERAL_CHANNEL"
    }

}