package com.tokopedia.chatbot.chatbot2.data.helpfullquestion

import com.google.gson.annotations.SerializedName

data class HelpFullQuestionPojo(
    @SerializedName("helpful_question")
    val helpfulQuestion: HelpfulQuestion?
) {
    data class HelpfulQuestion(
        @SerializedName("case_chat_id")
        val caseChatId: String?,
        @SerializedName("case_id")
        val caseId: String?,
        @SerializedName("helpful_questions")
        val helpfulQuestions: List<HelpfulQuestions>?,
        @SerializedName("user_id")
        val userId: Long?
    ) {
        data class HelpfulQuestions(
            @SerializedName("button_alias")
            val buttonAlias: String?,
            @SerializedName("text")
            val text: String?,
            @SerializedName("value")
            val value: Long?
        )
    }
}
