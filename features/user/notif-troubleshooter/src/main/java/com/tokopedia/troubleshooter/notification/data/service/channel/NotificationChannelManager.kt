package com.tokopedia.troubleshooter.notification.data.service.channel

interface NotificationChannelManager {
    fun getNotificationChannel(): Int
    fun hasNotificationChannel(): Boolean
}