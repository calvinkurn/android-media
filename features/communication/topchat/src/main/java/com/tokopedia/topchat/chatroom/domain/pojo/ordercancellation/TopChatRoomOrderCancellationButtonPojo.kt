package com.tokopedia.topchat.chatroom.domain.pojo.ordercancellation

import com.google.gson.annotations.SerializedName

data class TopChatRoomOrderCancellationButtonPojo(
    @SerializedName("title")
    val title: String = "",

    @SerializedName("description")
    val description: String = "",

    @SerializedName("reason")
    val reason: String = "",

    @SerializedName("primary_button_text")
    val primaryButton: String = "",

    @SerializedName("secondary_button_text")
    val secondaryButton: String = "",

    @SerializedName("app_link")
    val appLink: String = ""
)
