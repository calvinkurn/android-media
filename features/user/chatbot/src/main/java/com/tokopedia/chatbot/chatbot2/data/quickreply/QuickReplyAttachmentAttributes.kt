package com.tokopedia.chatbot.chatbot2.data.quickreply

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class QuickReplyAttachmentAttributes(
    @SerializedName("new_quick_replies")
    @Expose
    val quickReplies: List<QuickReplyPojo> = ArrayList()
)
