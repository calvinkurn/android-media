package com.tokopedia.chatbot.domain.pojo.dynamicAttachment

import com.google.gson.annotations.SerializedName

data class BigReplyBoxAttribute(
    @SerializedName("isActive")
    val isActive: Boolean,
    @SerializedName("placeholder")
    val placeholder: String,
    @SerializedName("title")
    val title: String
)
