package com.tokopedia.chatbot.chatbot2.data.livechatdivider

import com.google.gson.annotations.SerializedName

data class QuickRepliesItem(

    @SerializedName("action")
    val action: String?,

    @SerializedName("text")
    val text: String?,

    @SerializedName("value")
    val value: String?
)
