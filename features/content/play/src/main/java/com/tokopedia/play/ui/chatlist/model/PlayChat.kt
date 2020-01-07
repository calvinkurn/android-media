package com.tokopedia.play.ui.chatlist.model

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 04/12/19
 */
data class PlayChat(
        @SerializedName("channel_id")
        val channelId: String = "",
        @SerializedName("msg_id")
        val messageId: String = "",
        @SerializedName("message")
        val message: String = "",
        @SerializedName("user")
        val user: UserData = UserData()
) {

    data class UserData(
            @SerializedName("id")
            val id: String = "",
            @SerializedName("name")
            val name: String = "",
            @SerializedName("image")
            val image: String = ""
        )
}