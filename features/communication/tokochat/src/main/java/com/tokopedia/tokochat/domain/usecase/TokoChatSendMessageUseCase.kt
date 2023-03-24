package com.tokopedia.tokochat.domain.usecase

import com.gojek.conversations.babble.message.data.SendMessageMetaData
import com.gojek.conversations.extensions.ExtensionMessage
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokochat.tokochat_config_common.di.qualifier.TokoChatQualifier
import com.tokochat.tokochat_config_common.repository.TokoChatRepository
import javax.inject.Inject

class TokoChatSendMessageUseCase @Inject constructor(
    @TokoChatQualifier private val repository: TokoChatRepository
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
            channelUrl = channelUrl,
            extensionMessage = extensionMessage,
            onSuccess = onSuccess,
            onError = onError
        )
    }

    fun addTransientMessage(
        channel: String,
        extensionMessage: ExtensionMessage
    ) {
        repository.getConversationRepository().addTransientMessage(
            channel, extensionMessage
        )
    }

    fun sendTransientMessage(
        channel: String,
        extensionMessage: ExtensionMessage
    ) {
        repository.getConversationRepository().sendTransientMessage(
            channel, extensionMessage
        )
    }

    // Add transient, upload image, send transient
}
