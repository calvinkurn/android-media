package com.tokopedia.chatbot.chatbot2.data.dynamicAttachment

import com.google.gson.annotations.SerializedName

data class BigReplyBoxAttribute(
    @SerializedName("isActive")
    val isActive: Boolean,
    @SerializedName("placeholder")
    val placeholder: String,
    @SerializedName("title")
    val title: String
)
