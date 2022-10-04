package com.tokopedia.bubbles.data.model

class BubbleNotificationModel(
    val notificationType: Int,
    val notificationId: Int,
    val messageId: String,
    val applinks: String,
    val fullName: String,
    val avatarUrl: String,
    val summary: String,
    val sentTime: Long
)
