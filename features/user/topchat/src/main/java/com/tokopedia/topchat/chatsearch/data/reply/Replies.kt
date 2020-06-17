package com.tokopedia.topchat.chatsearch.data.reply


import com.google.gson.annotations.SerializedName

data class Replies(
        @SerializedName("count")
        val count: String = "",
        @SerializedName("data")
        val `data`: List<ChatReplyData> = listOf(),
        @SerializedName("hasNext")
        val hasNext: Boolean = false
)