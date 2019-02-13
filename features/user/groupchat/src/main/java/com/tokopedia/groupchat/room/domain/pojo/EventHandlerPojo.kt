package com.tokopedia.groupchat.room.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 09/10/18
 */
class EventHandlerPojo {

    @SerializedName("is_freeze")
    @Expose
    val isFreeze: Boolean = false
    @SerializedName("is_banned")
    @Expose
    val isBanned: Boolean = false
    @SerializedName("channel_id")
    @Expose
    val channelId: String? = null
    @SerializedName("user_id")
    @Expose
    val userId: String? = null

    companion object {

        val FREEZE = "freeze"
        val BANNED = "banned"
    }
}
