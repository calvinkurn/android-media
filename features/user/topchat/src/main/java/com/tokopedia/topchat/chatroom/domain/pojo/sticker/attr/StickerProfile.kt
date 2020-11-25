package com.tokopedia.topchat.chatroom.domain.pojo.sticker.attr


import com.google.gson.annotations.SerializedName

data class StickerProfile(
        @SerializedName("group_id")
        val groupId: String = "",
        @SerializedName("image_url")
        val imageUrl: String = "",
        @SerializedName("intention")
        val intention: String = "",
        @SerializedName("sticker_id")
        val stickerId: String = ""
) {

}