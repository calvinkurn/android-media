package com.tokopedia.chatbot.domain.pojo.replyBox

import com.google.gson.annotations.SerializedName

data class SmallReplyBoxAttribute(
    @SerializedName("isHidden")
    val isHidden: Boolean
)
