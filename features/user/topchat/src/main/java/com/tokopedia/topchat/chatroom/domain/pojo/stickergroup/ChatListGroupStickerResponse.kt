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

    fun isExpired(result: ChatListGroupStickerResponse): Boolean {
        return compareTo(result) > 0
    }

}