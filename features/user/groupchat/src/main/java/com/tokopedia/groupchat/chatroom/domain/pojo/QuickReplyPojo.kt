package com.tokopedia.groupchat.chatroom.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by StevenFredian .
 */

class QuickReplyPojo : BaseGroupChatPojo() {

    @SerializedName("quick_reply")
    @Expose
    var listQuickReply: List<String>? = null
}
