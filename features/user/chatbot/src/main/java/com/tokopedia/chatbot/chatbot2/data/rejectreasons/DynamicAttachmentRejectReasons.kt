package com.tokopedia.chatbot.chatbot2.data.rejectreasons

import com.google.gson.annotations.SerializedName

data class DynamicAttachmentRejectReasons(
    @SerializedName("helpful_question")
    val helpfulQuestion: RejectReasonHelpfulQuestion,
    @SerializedName("feedback_form")
    val feedbackForm: RejectReasonFeedbackForm
) {
    data class RejectReasonHelpfulQuestion(
        @SerializedName("message")
        val message: String = "",
        @SerializedName("new_quick_replies")
        val newQuickRepliesList: List<RejectReasonNewQuickReply> = emptyList(),
        @SerializedName("quick_replies")
        val quickReplies: List<RejectReasonQuickReply> = emptyList()
    ) {
        data class RejectReasonNewQuickReply(
            @SerializedName("action")
            val action: String,
            @SerializedName("text")
            val text: String,
            @SerializedName("value")
            val value: String
        )
        data class RejectReasonQuickReply(
            @SerializedName("message")
            val message: String
        )
    }
    data class RejectReasonFeedbackForm(
        @SerializedName("title")
        val title: String,
        @SerializedName("icon_tanya")
        val iconTanya: String,
        @SerializedName("reason_title")
        val reasonTitle: String,
        @SerializedName("text_box_placeholder")
        val textBoxPlaceHolder: String,
        @SerializedName("reason_minimum_character")
        val reasonMinimumCharacter: String,
        @SerializedName("reason_chip_list")
        val reasonChipList: List<RejectReasonReasonChip>
    ) {
        data class RejectReasonReasonChip(
            @SerializedName("code")
            val code: Long,
            @SerializedName("text")
            val text: String
        )
    }
}
