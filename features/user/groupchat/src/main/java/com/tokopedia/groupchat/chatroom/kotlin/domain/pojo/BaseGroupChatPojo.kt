package com.tokopedia.groupchat.chatroom.kotlin.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 09/10/18
 */
open class BaseGroupChatPojo {

    @SerializedName("channel_id")
    @Expose
    var channelId: String? = null

    @SerializedName("msg_id")
    @Expose
    var messageId: String? = null

    @SerializedName("timestamp")
    @Expose
    var timestampString: String? = null
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
