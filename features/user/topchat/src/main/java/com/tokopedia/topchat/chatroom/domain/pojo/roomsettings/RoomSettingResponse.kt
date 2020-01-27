package com.tokopedia.topchat.chatroom.domain.pojo.roomsettings


import com.google.gson.annotations.SerializedName

data class RoomSettingResponse(
    @SerializedName("chatRoomSettings")
    val chatRoomSettings: ChatRoomSettings = ChatRoomSettings()
)