package com.tokopedia.tokochat.domain.usecase

import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.channel.GetChannelRequest
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import com.tokopedia.tokochat.config.util.TokoChatResult
import com.tokopedia.tokochat.config.util.asFlowResult
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class TokoChatListUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {
    private var lastTimeStamp: Long = 0

    fun fetchAllCachedChannels(
        channelTypes: List<ChannelType>
    ): Flow<TokoChatResult<List<ConversationsChannel>>> {
        return flow {
            lastTimeStamp = 0 // reset
            val conversationRepository = repository.getConversationRepository()
            if (conversationRepository != null) {
                try {
                    emitAll(
                        conversationRepository.getAllCachedChannels(channelTypes)
                            .asFlowResult()
                    )
                } catch (throwable: Throwable) {
                    emit(TokoChatResult.Error(throwable))
                }
            } else {
                emit(TokoChatResult.Error(Throwable(ERROR_CONVERSATION_NULL)))
            }
        }
    }

    fun fetchAllRemoteChannels(
        channelTypes: List<ChannelType>,
        batchSize: Int
    ): Flow<TokoChatResult<List<ConversationsChannel>>> {
        if (lastTimeStamp < 0L) {
            return flowOf(
                TokoChatResult.Error(Throwable(ERROR_NEGATIVE_TIMESTAMP))
            )
        }
        return callbackFlow {
            repository.getConversationRepository()?.getAllChannels(
                getChannelRequest = GetChannelRequest(
                    types = channelTypes,
                    timestamp = getLastTimeStamp(),
                    batchSize = batchSize
                ),
                onSuccess = onSuccessFetchAllChannel(this),
                onError = onErrorFetchAllChannel(this)
            )
            awaitClose { channel.close() }
        }
    }

    private fun onSuccessFetchAllChannel(
        scope: ProducerScope<TokoChatResult<List<ConversationsChannel>>>
    ): (List<ConversationsChannel>) -> Unit {
        return { list ->
            list.lastOrNull()?.createdAt?.let {
                lastTimeStamp = it
            }
            scope.trySend(TokoChatResult.Success(list))
        }
    }

    private fun onErrorFetchAllChannel(
        scope: ProducerScope<TokoChatResult<List<ConversationsChannel>>>
    ): (ConversationsNetworkError?) -> Unit {
        return {
            scope.trySend(
                TokoChatResult.Error(
                    it ?: MessageErrorException(
                        ERROR_FETCH_CHANNELS
                    )
                )
            )
        }
    }

    private fun getLastTimeStamp(): Long {
        return if (lastTimeStamp == 0L) {
            System.currentTimeMillis()
        } else {
            lastTimeStamp
        }
    }

    companion object {
        private const val ERROR_FETCH_CHANNELS = "Error fetch all channels"
        private const val ERROR_NEGATIVE_TIMESTAMP = "Last time stamp should not negative"
        private const val ERROR_CONVERSATION_NULL = "ConversationRepository is null"
    }
}
