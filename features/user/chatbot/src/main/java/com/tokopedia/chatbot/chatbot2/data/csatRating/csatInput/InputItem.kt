package com.tokopedia.chatbot.chatbot2.data.csatRating.csatInput

import com.google.gson.annotations.SerializedName

data class InputItem(

    @SerializedName("score")
    var score: Int? = null,

    @SerializedName("reason")
    var reason: String? = null,

    @SerializedName("trigger_rule_type")
    var triggerRuleType: String? = null,

    @SerializedName("chatbot_session_id")
    var chatbotSessionId: String? = null,

    @SerializedName("livechat_session_id")
    var livechatSessionId: String? = null,

    @SerializedName("other_reason")
    var otherReason: String? = null,

    @SerializedName("timestamp")
    var timestamp: String? = null

)
