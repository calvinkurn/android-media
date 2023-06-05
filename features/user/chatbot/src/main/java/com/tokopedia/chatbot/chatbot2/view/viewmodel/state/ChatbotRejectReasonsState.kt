package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

import com.tokopedia.chatbot.chatbot2.data.rejectreasons.DynamicAttachmentRejectReasons

sealed class ChatbotRejectReasonsState {
    data class ChatbotRejectReasonData(
        val rejectReasons: DynamicAttachmentRejectReasons
    ) : ChatbotRejectReasonsState()
}
