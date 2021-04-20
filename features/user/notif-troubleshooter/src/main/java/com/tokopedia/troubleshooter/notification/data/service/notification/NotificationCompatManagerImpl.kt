package com.tokopedia.troubleshooter.notification.data.service.notification

import android.app.NotificationManager
import android.app.NotificationManager.INTERRUPTION_FILTER_ALL
import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat

class NotificationCompatManagerImpl(
        private val context: Context
): NotificationCompatManager {

    override fun isNotificationEnabled(): Boolean {
        return NotificationManagerCompat
                .from(context)
                .areNotificationsEnabled()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun isDndModeEnabled(): Boolean {
//        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        return manager.currentInterruptionFilter == INTERRUPTION_FILTER_ALL
        return Settings.Global.getInt(context.contentResolver, "zen_mode") != 0
    }

}