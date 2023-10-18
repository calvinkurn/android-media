package com.tokopedia.chatbot.chatbot2.data.livechatdivider
import com.google.gson.annotations.SerializedName

data class AgentQueue(

    @SerializedName("label")
    val label: String?,

    @SerializedName("type")
    val type: String?
)
