package com.tokopedia.groupchat.chatroom.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 08/10/18
 */
class UserMsg : BaseGroupChatPojo() {
    @SerializedName("message")
    @Expose
    var message: String = ""


}
