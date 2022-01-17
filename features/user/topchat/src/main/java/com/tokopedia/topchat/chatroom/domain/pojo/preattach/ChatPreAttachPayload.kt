package com.tokopedia.topchat.chatroom.domain.pojo.preattach


import com.google.gson.annotations.SerializedName

data class ChatPreAttachPayload(
    @SerializedName("list")
    var list: List<Attachment> = listOf()
)