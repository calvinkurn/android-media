package com.tokopedia.chatbot.chatbot2.data.livechatdivider

import com.google.gson.annotations.SerializedName

data class LiveChatDividerAttributes(

    @SerializedName("devider")
    val divider: Divider?,

    @SerializedName("agent_queue")
    val agentQueue: AgentQueue?
)
