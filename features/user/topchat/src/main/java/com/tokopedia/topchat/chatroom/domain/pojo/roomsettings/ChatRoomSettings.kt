package com.tokopedia.topchat.chatroom.domain.pojo.roomsettings


import com.google.gson.annotations.SerializedName

data class ChatRoomSettings(
    @SerializedName("banner")
        val bannerUiModel: RoomSettingBannerUiModel = RoomSettingBannerUiModel(),
    @SerializedName("fraudAlert")
        val fraudAlert: RoomSettingFraudAlertUiModel = RoomSettingFraudAlertUiModel()
)