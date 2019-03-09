package com.tokopedia.groupchat.chatroom.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by StevenFredian on 03/05/18.
 */

data class PinnedMessagePojo(
        
        @SerializedName("message")
        @Expose
        var message: String = "",
        @SerializedName("image_url")
        @Expose
        var imageUrl: String = "",
        @SerializedName("redirect_url")
        @Expose
        var redirectUrl: String = "",
        @SerializedName("title")
        @Expose
        var title: String = ""
) : BaseGroupChatPojo() {
}
