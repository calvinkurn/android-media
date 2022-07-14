package com.tokopedia.topchat.chatroom.domain.pojo.stickergroup


import com.google.gson.annotations.SerializedName

data class ChatListGroupSticker(
        @SerializedName("list")
        val list: List<StickerGroup> = listOf()
)