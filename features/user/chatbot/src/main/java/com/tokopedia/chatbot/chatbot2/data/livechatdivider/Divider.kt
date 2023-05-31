package com.tokopedia.chatbot.chatbot2.data.livechatdivider

import com.google.gson.annotations.SerializedName

data class Divider(

    @SerializedName("quick_replies")
    val quickReplies: List<QuickRepliesItem?>?,

    @SerializedName("label")
    val label: String?
)
