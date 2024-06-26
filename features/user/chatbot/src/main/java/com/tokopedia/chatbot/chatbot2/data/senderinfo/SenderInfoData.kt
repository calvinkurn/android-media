package com.tokopedia.chatbot.chatbot2.data.senderinfo

import com.google.gson.annotations.SerializedName

data class SenderInfoData(
    @SerializedName("icon_url")
    val iconUrl: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("icon_url_dark")
    val iconDarkUrl: String?
)
