package com.tokopedia.tokochat.domain.usecase

import com.gojek.conversations.babble.message.data.SendMessageMetaData
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
}
