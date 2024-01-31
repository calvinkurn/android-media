package com.tokopedia.chatbot.chatbot2.csat.data.response

import com.google.gson.annotations.SerializedName

data class DynamicCsatWs(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("service")
    val service: String = "",
    @SerializedName("minimum_other_reason_char")
    val minimumOtherReasonChar: Int = 0,
    @SerializedName("points")
    val points: List<PointWs> = emptyList()
)

data class PointWs(
    @SerializedName("score")
    val score: Int = 0,
    @SerializedName("caption")
    val caption: String = "",
    @SerializedName("other_reason_title")
    val otherReasonTitle: String = "",
    @SerializedName("reason_title")
    val reasonTitle: String = "",
    @SerializedName("reasons")
    val reasons: List<String> = emptyList()
)
