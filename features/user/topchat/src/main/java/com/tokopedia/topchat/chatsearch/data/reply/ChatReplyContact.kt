package com.tokopedia.topchat.chatsearch.data.reply


import com.google.gson.annotations.SerializedName

data class ChatReplyContact(
        @SerializedName("attributes")
        val attributes: ChatReplyAttributes = ChatReplyAttributes(),
        @SerializedName("id")
        val id: Int = 0,
        @SerializedName("role")
        val role: String = ""
)