package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

sealed class ChatbotDynamicAttachmentMediaButtonState {
    data class OnReceiveMediaButtonAttachment(
        val toShowAddAttachmentButton: Boolean,
        val toShowImageUploadButton: Boolean,
        val toShowVideoUploadButton: Boolean
    ): ChatbotDynamicAttachmentMediaButtonState()
}
