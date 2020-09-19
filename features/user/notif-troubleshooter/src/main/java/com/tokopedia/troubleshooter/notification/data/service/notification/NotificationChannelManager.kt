package com.tokopedia.troubleshooter.notification.data.service.notification

interface NotificationChannelManager {
    fun getNotificationChannel(): Int?
    fun hasNotificationChannel(): Boolean
    fun isImportanceChannel(): Boolean
    fun isNotificationChannelEnabled(): Boolean
}