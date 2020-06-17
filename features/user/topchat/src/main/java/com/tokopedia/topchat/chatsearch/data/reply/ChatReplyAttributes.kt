package com.tokopedia.topchat.chatsearch.data.reply


import com.google.gson.annotations.SerializedName

data class ChatReplyAttributes(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("thumbnail")
    val thumbnail: String = ""
)