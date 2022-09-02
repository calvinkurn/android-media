package com.tokopedia.topchat.chatroom.domain.pojo.roomsettings


import com.google.gson.annotations.SerializedName

data class RoomSettingResponse(
        @SerializedName("chatRoomSettings")
        val chatRoomSettings: ChatRoomSettings = ChatRoomSettings()
) {
    val showFraudAlert get() = chatRoomSettings.fraudAlert.enable
    val showBanner get() = chatRoomSettings.bannerUiModel.enable
    val fraudAlert get() = chatRoomSettings.fraudAlert
    val roomBanner get() = chatRoomSettings.bannerUiModel
}