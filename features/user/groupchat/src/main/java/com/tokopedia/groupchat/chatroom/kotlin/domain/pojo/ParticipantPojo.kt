package com.tokopedia.groupchat.chatroom.kotlin.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author : Steven 06/11/18
 */
class ParticipantPojo : BaseGroupChatPojo() {

    @SerializedName("total_view")
    @Expose
    val totalView: String? = null
}
