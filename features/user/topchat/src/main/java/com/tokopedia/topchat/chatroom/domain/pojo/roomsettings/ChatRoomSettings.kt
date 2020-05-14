package com.tokopedia.topchat.chatroom.domain.pojo.roomsettings


import com.google.gson.annotations.SerializedName

data class ChatRoomSettings(
        @SerializedName("banner")
        val banner: RoomSettingBanner = RoomSettingBanner(),
        @SerializedName("fraudAlert")
        val fraudAlert: RoomSettingFraudAlert = RoomSettingFraudAlert()
)