package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

import com.tokopedia.chatbot.chatbot2.view.uimodel.videoupload.VideoUploadUiModel

sealed class ChatbotVideoUploadFailureState {
    data class ChatbotVideoUploadFailure(val uiModel: VideoUploadUiModel, val message: String) : ChatbotVideoUploadFailureState()
}
