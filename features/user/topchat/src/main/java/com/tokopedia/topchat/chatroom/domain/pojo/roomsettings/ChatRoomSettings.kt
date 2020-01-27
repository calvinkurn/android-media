package com.tokopedia.topchat.chatroom.domain.pojo.roomsettings


import com.google.gson.annotations.SerializedName

data class ChatRoomSettings(
    @SerializedName("banner")
    val banner: Banner = Banner(),
    @SerializedName("fraudAlert")
    val fraudAlert: FraudAlert = FraudAlert()
)