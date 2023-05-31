package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

import com.tokopedia.chatbot.chatbot2.data.reject_reasons.DynamicAttachmentRejectReasons

sealed class ChatbotRejectReasonsState {
    data class ChatbotRejectReasonData(
        val rejectReasonHelpfulQuestion: DynamicAttachmentRejectReasons.RejectReasonHelpfulQuestion,
        val feedbackForm: DynamicAttachmentRejectReasons.RejectReasonFeedbackForm
    ) : ChatbotRejectReasonsState()
}
