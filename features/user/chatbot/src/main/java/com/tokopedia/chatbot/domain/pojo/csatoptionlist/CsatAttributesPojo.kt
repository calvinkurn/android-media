package com.tokopedia.chatbot.domain.pojo.csatoptionlist


import com.google.gson.annotations.SerializedName
import com.tokopedia.chatbot.domain.pojo.helpfullquestion.HelpFullQuestionPojo

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
        val userId: Int?
    ) {
        data class Point(
            @SerializedName("caption")
            val caption: String?,
            @SerializedName("description")
            val description: String?,
            @SerializedName("score")
            val score: Int = 0
        )

        data class Reason(
            @SerializedName("code")
            val code: Int?,
            @SerializedName("text")
            val text: String?
        )
    }
}