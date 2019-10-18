package com.tokopedia.core.drawer2.data.pojo;

import com.google.gson.annotations.SerializedName;

data class ChatNotification(
        @SerializedName("notifications")
        val notifications: Notifications
) {
    data class Notifications(
            @SerializedName("chat")
            val chat: Chat
    ) {
        data class Chat(
                @SerializedName("unreads")
                val unreads: Int,
                @SerializedName("unreadsSeller")
                val unreadsSeller: Int,
                @SerializedName("unreadsUser")
                val unreadsUser: Int
        )
    }
}