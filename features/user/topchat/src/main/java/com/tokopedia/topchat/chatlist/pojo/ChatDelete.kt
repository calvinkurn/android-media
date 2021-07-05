package com.tokopedia.topchat.chatlist.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ChatDeleteStatus(
        @Expose @SerializedName("chatMoveToTrash") var chatMoveToTrash: ChatDeleteList = ChatDeleteList(listOf())
)

data class ChatDeleteList(
        @Expose @SerializedName("list") var list: List<ChatDelete>
)

data class ChatDelete(
        @Expose @SerializedName("IsSuccess") var isSuccess: Int,
        @Expose @SerializedName("DetailResponse") var detailResponse: String,
        @Expose @SerializedName("MsgID") var messageId: Long
)