package com.tokopedia.chat_service.domain

import com.gojek.conversations.babble.message.data.SendMessageMetaData
import com.gojek.conversations.extensions.ExtensionMessage
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.chat_service.data.repository.TokoChatRepository
import javax.inject.Inject

class SendMessageUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {
    fun sendTextMessage(
        channelUrl: String,
        text: String,
        sendMessageMetaData: SendMessageMetaData
    ) {
        repository.getConversationRepository().sendTextMessage(
            channelUrl, text, sendMessageMetaData
        )
    }

    fun sendExtensionMessage(
        channelUrl: String,
        extensionMessage: ExtensionMessage,
        onSuccess: () -> Unit = {},
        onError: (error: ConversationsNetworkError) -> Unit = {}
    ) {
        repository.getConversationRepository().sendExtensionMessage(
            channelUrl, extensionMessage, onSuccess, onError
        )
    }
}
