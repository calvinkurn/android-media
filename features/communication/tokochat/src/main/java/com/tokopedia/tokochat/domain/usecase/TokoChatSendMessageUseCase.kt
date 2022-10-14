package com.tokopedia.tokochat.domain.usecase

import com.gojek.conversations.babble.message.data.SendMessageMetaData
import com.tokopedia.tokochat.data.repository.TokoChatRepository
import javax.inject.Inject

class TokoChatSendMessageUseCase @Inject constructor(
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
}
