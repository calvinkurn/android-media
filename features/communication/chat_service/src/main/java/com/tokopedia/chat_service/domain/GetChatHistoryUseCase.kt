package com.tokopedia.chat_service.domain

import com.gojek.conversations.database.chats.ConversationsMessage
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_service.data.repository.ChatServiceRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import com.tokopedia.network.exception.MessageErrorException
import javax.inject.Inject

class GetChatHistoryUseCase @Inject constructor(
    private val repository: ChatServiceRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<String, List<ConversationsMessage>>(dispatcher.io){

    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: String): List<ConversationsMessage> {
        return repository.conversationRepository?.getChatHistory(params)?.value
            ?: throw MessageErrorException("Repository should not null")
    }
}