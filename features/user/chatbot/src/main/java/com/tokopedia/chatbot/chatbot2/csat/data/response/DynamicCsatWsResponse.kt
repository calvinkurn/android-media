package com.tokopedia.chatbot.chatbot2.csat.data.response

import com.google.gson.annotations.SerializedName

data class DynamicCsatWs(
    @SerializedName("trigger_rule_type")
    val triggerRuleType: String = "",
    @SerializedName("chatbot_session_id")
    val chatbotSessionId: String = "",
    @SerializedName("livechat_session_id")
    val livechatSessionId: String = "",
    @SerializedName("category")
    val category: String = "",
    @SerializedName("minimum_other_reason_char")
    val minimumOtherReasonChar: Int = 0,
    @SerializedName("maximum_other_reason_char")
    val maximumOtherReasonChar: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("other_reason_title")
    val otherReasonTitle: String = "",
    @SerializedName("points")
    val points: List<PointWs> = emptyList()
)

data class PointWs(
    @SerializedName("score")
    val score: Int = 0,
    @SerializedName("caption")
    val caption: String = "",
    @SerializedName("reason_title")
    val reasonTitle: String = "",
    @SerializedName("reasons")
    val reasons: List<String> = emptyList()
)
