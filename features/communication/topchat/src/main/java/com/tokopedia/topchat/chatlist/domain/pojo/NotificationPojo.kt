package com.tokopedia.topchat.chatlist.domain.pojo

import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 2019-08-08
 */
data class NotificationsPojo(
    @SerializedName("notifications")
    var notification: Notification = Notification()
) {
    data class Notification(
        @SerializedName("chat")
        var chat: Chat = Chat()
    ) {
        data class Chat(
            @SerializedName("unreadsSeller")
            var unreadsSeller: Long = 0,
            @SerializedName("unreadsUser")
            var unreadsUser: Long = 0
        )
    }
}
