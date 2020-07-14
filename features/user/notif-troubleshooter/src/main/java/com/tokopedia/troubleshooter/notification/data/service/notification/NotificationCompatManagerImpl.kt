package com.tokopedia.troubleshooter.notification.data.service.notification

import android.content.Context
import androidx.core.app.NotificationManagerCompat

class NotificationCompatManagerImpl(
        private val context: Context
): NotificationCompatManager {

    override fun isNotificationEnabled(): Boolean {
        return NotificationManagerCompat
                .from(context)
                .areNotificationsEnabled()
    }

}