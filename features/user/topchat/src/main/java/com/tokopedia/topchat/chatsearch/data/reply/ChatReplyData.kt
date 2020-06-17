package com.tokopedia.topchat.chatsearch.data.reply


import com.google.gson.annotations.SerializedName

data class ChatReplyData(
        @SerializedName("contact")
        val contact: ChatReplyContact = ChatReplyContact(),
        @SerializedName("lastMessage")
        val lastMessage: String = "",
        @SerializedName("msgId")
        val msgId: Int = 0,
        @SerializedName("productId")
        val productId: String = "",
        @SerializedName("replyId")
        val replyId: Int = 0
)