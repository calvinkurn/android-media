package com.tokopedia.topchat.chatroom.domain.pojo.sticker.attr


import com.google.gson.annotations.SerializedName

data class StickerAttributesResponse(
        @SerializedName("sticker_profile")
        val stickerProfile: StickerProfile = StickerProfile()
)