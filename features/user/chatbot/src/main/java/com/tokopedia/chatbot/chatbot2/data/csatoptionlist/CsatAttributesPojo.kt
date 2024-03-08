package com.tokopedia.chatbot.chatbot2.data.csatoptionlist

import com.google.gson.annotations.SerializedName
import com.tokopedia.csat_rating.dynamiccsat.data.model.DynamicCsat

data class CsatAttributesPojo(
    @SerializedName("csat")
    val csat: Csat?
) {
    data class Csat(
        @SerializedName("case_chat_id")
        val caseChatId: String?,
        @SerializedName("case_id")
        val caseId: String?,
        @SerializedName("points")
        val points: List<Point>?,
        @SerializedName("reason_title")
        val reasonTitle: String?,
        @SerializedName("reasons")
        val reasons: List<Reason>?,
        @SerializedName("title")
        val title: String?,
        @SerializedName("trigger_rule_type")
        val triggerRuleType: String?,
        @SerializedName("user_id")
        val userId: Long?,
        @SerializedName("dynamic_csat")
        val dynamicCsat: DynamicCsat?
    ) {
        data class Point(
            @SerializedName("caption")
            val caption: String?,
            @SerializedName("description")
            val description: String?,
            @SerializedName("score")
            val score: Long = 0
        )
        data class Reason(
            @SerializedName("code")
            val code: Long?,
            @SerializedName("text")
            val text: String?
        )
    }
}
