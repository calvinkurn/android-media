package com.tokopedia.groupchat.chatroom.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 09/10/18
 */
open class BaseGroupChatPojo {

    @SerializedName("channel_id")
    @Expose
    var channelId: String = ""

    @SerializedName("msg_id")
    @Expose
    var messageId: String = ""

    @SerializedName("timestamp")
    @Expose
    var timestampString: String = ""
        private set

    @SerializedName("user")
    @Expose
    var user: UserData? = null

    val timestamp: Long?
        get() = java.lang.Long.valueOf(timestampString)

    fun setTimestamp(timestamp: String) {
        this.timestampString = timestamp
    }
}
