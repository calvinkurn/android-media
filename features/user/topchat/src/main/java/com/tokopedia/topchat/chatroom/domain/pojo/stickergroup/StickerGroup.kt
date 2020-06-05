package com.tokopedia.topchat.chatroom.domain.pojo.stickergroup


import com.google.gson.annotations.SerializedName

data class StickerGroup(
        @SerializedName("groupUUID")
        val groupUUID: String = "",
        @SerializedName("lastUpdate")
        val lastUpdate: String = "",
        @SerializedName("thumbnail")
        val thumbnail: String = "",
        @SerializedName("title")
        val title: String = ""
)