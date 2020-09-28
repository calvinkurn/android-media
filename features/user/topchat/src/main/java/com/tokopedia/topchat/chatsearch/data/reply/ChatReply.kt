package com.tokopedia.topchat.chatsearch.data.reply


import com.google.gson.annotations.SerializedName

data class ChatReply(
    @SerializedName("replies")
    val replies: Replies = Replies()
)