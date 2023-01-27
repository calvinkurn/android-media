package com.tokopedia.chatbot.chatbot2.view.viewmodel.state

import com.tokopedia.chat_common.data.ImageUploadUiModel

sealed class ChatbotImageUploadFailureState {
    data class ImageUploadErrorBody(val throwable: Throwable, val imageUploadUiModel: ImageUploadUiModel)
}
