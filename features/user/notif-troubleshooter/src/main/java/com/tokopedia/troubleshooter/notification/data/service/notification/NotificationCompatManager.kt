package com.tokopedia.troubleshooter.notification.data.service.notification

interface NotificationCompatManager {
    fun isNotificationEnabled(): Boolean
    fun isDndModeEnabled(): Boolean
}