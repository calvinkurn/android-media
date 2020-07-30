package com.tokopedia.play.broadcaster.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.play.broadcaster.socket.PlaySocketEnum
import com.tokopedia.play.broadcaster.socket.PlaySocketType


/**
 * Created by mzennis on 15/07/20.
 */
data class Chat(
        @SerializedName("channel_id")
        val channelId: String = "",
        @SerializedName("msg_id")
        val messageId: String = "",
        @SerializedName("message")
        val message: String = "",
        @SerializedName("user")
        val user: UserData = UserData()
): PlaySocketType {

    override val type: PlaySocketEnum get() = PlaySocketEnum.Chat

    data class UserData(
            @SerializedName("id")
            val id: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("image")
            val image: String = ""
    )
}