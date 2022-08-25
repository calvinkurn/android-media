package com.tokopedia.chat_service.domain

import com.gojek.conversations.babble.channel.data.CreateChannelInfo
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.chat_service.data.repository.ChatServiceRepository
import com.tokopedia.graphql.domain.coroutine.CoroutineUseCase
import javax.inject.Inject

class CreateChannelUseCase @Inject constructor(
    private val repository: ChatServiceRepository,
    dispatcher: CoroutineDispatchers
) : CoroutineUseCase<CreateChannelUseCase.Param, Unit>(dispatcher.io){
    override fun graphqlQuery(): String = ""

    override suspend fun execute(params: Param) {
        repository.conversationRepository?.createChannel(
            params.createChannelInfo,
            params.onSuccess,
            params.onError
        )
    }

    data class Param(
        val createChannelInfo: CreateChannelInfo,
        val onSuccess: (channel: ConversationsChannel) -> Unit,
        val onError: (error: ConversationsNetworkError) -> Unit
    )

}