package com.tokopedia.chatbot.chatbot2.data.quickreplysend

import com.google.gson.annotations.SerializedName

data class QuickReplySendAttachmentAttributes(
    @SerializedName("is_typing_blocked")
    val isTypingBlocked: Boolean = false
)
