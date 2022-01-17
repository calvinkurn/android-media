package com.tokopedia.topchat.chatroom.domain.pojo.preattach


import com.google.gson.annotations.SerializedName

data class PreAttachPayloadResponse(
    @SerializedName("chatPreAttachPayload")
    var chatPreAttachPayload: ChatPreAttachPayload = ChatPreAttachPayload()
)