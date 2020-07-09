package com.tokopedia.topchat.chatroom.domain.pojo.stickergroup


import com.google.gson.annotations.SerializedName

data class ChatListGroupStickerResponse(
        @SerializedName("chatListGroupSticker")
        val chatListGroupSticker: ChatListGroupSticker = ChatListGroupSticker()
) {

    val stickerGroups get() = chatListGroupSticker.list

    fun hasDifferentGroupSize(cache: ChatListGroupStickerResponse?): Boolean {
        return cache == null || cache.stickerGroups.size != stickerGroups.size
    }

}