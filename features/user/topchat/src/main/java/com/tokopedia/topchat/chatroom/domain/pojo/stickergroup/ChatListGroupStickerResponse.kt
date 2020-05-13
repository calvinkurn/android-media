package com.tokopedia.topchat.chatroom.domain.pojo.stickergroup


import androidx.collection.ArrayMap
import com.google.gson.annotations.SerializedName

data class ChatListGroupStickerResponse(
        @SerializedName("chatListGroupSticker")
        val chatListGroupSticker: ChatListGroupSticker = ChatListGroupSticker()
) : Comparable<ChatListGroupStickerResponse> {

    override fun compareTo(other: ChatListGroupStickerResponse): Int {
        val arrayMap = ArrayMap<String, String>()
        chatListGroupSticker.list.forEach { stickerGroup ->
            arrayMap[stickerGroup.groupUUID] = stickerGroup.lastUpdate
        }
        other.chatListGroupSticker.list.forEach { stickerGroup ->
            if (arrayMap[stickerGroup.groupUUID] != stickerGroup.lastUpdate) return 1
        }
        return 0
    }

    fun hasExpiredCache(cache: ChatListGroupStickerResponse?): Boolean {
        return cache != null && compareTo(cache) > 0
    }

}