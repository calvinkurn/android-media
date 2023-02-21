package com.tokopedia.topchat.chatroom.domain.pojo.imageserver

import com.google.gson.annotations.SerializedName

data class ChatImageServerResponse(
    @SerializedName("chatImageServer")
    val chatImageServer: ChatImageServer = ChatImageServer()
) {
    val sourceId get() = chatImageServer.sourceID
    val sourceIdSecure get() = chatImageServer.sourceIDSecure
}
