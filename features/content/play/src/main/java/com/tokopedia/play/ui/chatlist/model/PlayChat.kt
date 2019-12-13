package com.tokopedia.play.ui.chatlist.model

import com.google.gson.annotations.SerializedName

/**
 * Created by jegul on 04/12/19
 */
data class PlayChat(
        @SerializedName("channel_id")
        var channelId: String = "",
        @SerializedName("msg_id")
        var messageId: String = "",
        @SerializedName("message")
        var message: String = "",
        @SerializedName("user")
        var user: UserData = UserData()
) {

    data class UserData(
            @SerializedName("id")
            var id: String = "",
            @SerializedName("name")
            var name: String = "",
            @SerializedName("image")
            var image: String = ""
        )
}