package com.tokopedia.topchat.chatlist.domain.pojo

import com.google.gson.annotations.SerializedName

data class ChatDeleteStatus(
        @SerializedName("chatMoveToTrash")
        var chatMoveToTrash: ChatDeleteList = ChatDeleteList(listOf())
)

data class ChatDeleteList(
        @SerializedName("list")
        var list: List<ChatDelete>
)

data class ChatDelete(
        @SerializedName("IsSuccess")
        var isSuccess: Int,
        @SerializedName("DetailResponse")
        var detailResponse: String,
        @SerializedName("MsgID")
        var messageId: String
)