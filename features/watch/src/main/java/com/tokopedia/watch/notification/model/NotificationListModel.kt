package com.tokopedia.watch.notification.model


data class NotificationListModel(
    val notifications: List<Notification> = listOf()
)

data class Notification(
    val notifId: String = "",
    val title: String = "",
    val content: String = ""
)
