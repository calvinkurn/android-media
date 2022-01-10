package com.tokopedia.topchat.chatroom.domain.pojo.sticker


import com.google.gson.annotations.SerializedName

data class ChatBundleSticker(
        @SerializedName("hasNext")
        val hasNext: Boolean = false,
        @SerializedName("list")
        var list: List<Sticker> = listOf(),
        @SerializedName("maxUUID")
        val maxUUID: String = ""
)