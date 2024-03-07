package com.tokopedia.chatbot.chatbot2.csat.data.response

import com.google.gson.annotations.SerializedName

data class DynamicCsat(
    @SerializedName("title")
    val title: String = "",
    @SerializedName("service")
    val service: String = "",
    @SerializedName("minimum_other_reason_char", alternate = ["minimumOtherReasonChar"])
    val minimumOtherReasonChar: Int = 0,
    @SerializedName("points")
    val points: List<Point> = emptyList()
)

data class Point(
    @SerializedName("score")
    val score: Int = 0,
    @SerializedName("caption")
    val caption: String = "",
    @SerializedName("other_reason_title", alternate = ["otherReasonTitle"])
    val otherReasonTitle: String = "",
    @SerializedName("reason_title", alternate = ["reasonTitle"])
    val reasonTitle: String = "",
    @SerializedName("reasons")
    val reasons: List<String> = emptyList()
)
