package com.tokopedia.topchat.chatroom.domain.pojo.sticker


import com.google.gson.annotations.SerializedName

data class Sticker(
        @SerializedName("groupUUID")
        val groupUUID: String = "",
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("intention")
        val intention: String = "",
        @SerializedName("stickerUUID")
        val stickerUUID: String = ""
)