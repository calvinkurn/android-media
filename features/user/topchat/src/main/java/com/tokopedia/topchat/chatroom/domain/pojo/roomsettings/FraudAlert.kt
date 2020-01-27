package com.tokopedia.topchat.chatroom.domain.pojo.roomsettings


import com.google.gson.annotations.SerializedName

data class FraudAlert(
    @SerializedName("enable")
    val enable: Boolean = false,
    @SerializedName("imageUrl")
    val imageUrl: String = "",
    @SerializedName("text")
    val text: String = ""
)