package com.tokopedia.chatbot.chatbot2.data.rejectreasons

import com.google.gson.annotations.SerializedName

data class DynamicAttachmentRejectReasonsSend(
    @SerializedName("helpful_question_feedback_form")
    val helpfulQuestionFeedbackForm: HelpfulQuestionFeedbackForm
) {
    data class HelpfulQuestionFeedbackForm(
        @SerializedName("helpful_question")
        val helpfulQuestion: FeedbackFormHelpfulQuestion,
        @SerializedName("feedback_form")
        val feedbackForm: FeedbackForm
    ) {
        data class FeedbackFormHelpfulQuestion(
            @SerializedName("new_quick_replies")
            val newQuickReplies: List<DynamicAttachmentRejectReasons.RejectReasonHelpfulQuestion.RejectReasonNewQuickReply>
        )
        data class FeedbackForm(
            @SerializedName("reason")
            val reason: String,
            @SerializedName("reason_chip_code_list")
            val list: List<Long>
        )
    }
}
