package com.tokopedia.topchat.chatroom.domain.pojo.sticker


import com.google.gson.annotations.SerializedName

data class StickerResponse(
        @SerializedName("chatBundleSticker")
        val chatBundleSticker: ChatBundleSticker = ChatBundleSticker()
)