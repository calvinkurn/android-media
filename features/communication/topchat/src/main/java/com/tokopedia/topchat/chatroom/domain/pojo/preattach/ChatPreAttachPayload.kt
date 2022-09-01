package com.tokopedia.topchat.chatroom.domain.pojo.preattach


import com.google.gson.annotations.SerializedName
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.Attachment

data class ChatPreAttachPayload(
    @SerializedName("list")
    var list: List<Attachment> = listOf()
)