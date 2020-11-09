package com.tokopedia.troubleshooter.notification.data.service.notification

interface NotificationChannelManager {
    fun notificationChannel(): Int
    fun hasNotificationChannel(): Boolean
    fun isImportanceChannel(): Boolean
    fun isNotificationChannelEnabled(): Boolean
}