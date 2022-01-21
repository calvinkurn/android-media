package com.tokopedia.inbox.domain.data.notification


import com.google.gson.annotations.SerializedName

data class InboxNotificationResponse(
    @SerializedName("notifications")
    val notifications: Notifications = Notifications()
)