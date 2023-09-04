package com.tokopedia.tokochat.domain.usecase

import com.gojek.conversations.babble.message.data.SendMessageMetaData
import com.gojek.conversations.extensions.ExtensionMessage
import com.tokopedia.tokochat.config.di.qualifier.TokoChatQualifier
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import javax.inject.Inject

open class TokoChatSendMessageUseCase @Inject constructor(
    @TokoChatQualifier private val repository: TokoChatRepository
) {
    fun sendTextMessage(
        channelUrl: String,
        text: String,
        sendMessageMetaData: SendMessageMetaData
    ) {
        repository.getConversationRepository()?.sendTextMessage(
            channelUrl,
            text,
            sendMessageMetaData
        )
    }

    open fun addTransientMessage(
        channel: String,
        extensionMessage: ExtensionMessage
    ) {
        repository.getConversationRepository()?.addTransientMessage(
            channel,
            extensionMessage
        )
    }

    open fun sendTransientMessage(
        channel: String,
        extensionMessage: ExtensionMessage
    ) {
        repository.getConversationRepository()?.sendTransientMessage(
            channel,
            extensionMessage
        )
    }

    open fun setTransientMessageFailed(messageId: String) {
        repository.getConversationRepository()?.setTransientMessageFailed(
            messageId = messageId
        )
    }
}
