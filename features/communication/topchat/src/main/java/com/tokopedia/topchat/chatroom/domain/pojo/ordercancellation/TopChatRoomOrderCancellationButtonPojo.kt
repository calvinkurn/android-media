package com.tokopedia.topchat.chatroom.domain.pojo.ordercancellation

import com.google.gson.annotations.SerializedName

data class TopChatRoomOrderCancellationButtonPojo(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("app_link")
    val appLink: String = ""
)
