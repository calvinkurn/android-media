package com.tokopedia.topchat.chatlist.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 2019-08-08
 */
data class NotificationsPojo(
        @SerializedName("notifications")
        @Expose
        var chatNotifications: ChatNotificationsPojo = ChatNotificationsPojo()
)

data class ChatNotificationsPojo(
        @SerializedName("chat")
        @Expose
        var chatTabCounter: ChatTabCounterPojo = ChatTabCounterPojo()
)

data class ChatTabCounterPojo(
        @SerializedName("unreadsSeller")
        @Expose
        var unreadsSeller: Long = 0,
        @SerializedName("unreadsUser")
        @Expose
        var unreadsUser: Long = 0
)
