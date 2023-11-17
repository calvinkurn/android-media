package com.tokopedia.tokochat.domain.usecase

import androidx.lifecycle.asFlow
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.channel.GetChannelRequest
import com.gojek.conversations.network.ConversationsNetworkError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import com.tokopedia.tokochat.config.util.TokoChatResult
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TokoChatListUseCase @Inject constructor(
    private val repository: TokoChatRepository
) {
    private var lastTimeStamp: Long = 0

    fun fetchAllCachedChannels(
        channelTypes: List<ChannelType>,
        defaultBatchSize: Int
    ): Flow<TokoChatResult<List<ConversationsChannel>>> {
        return flow {
            lastTimeStamp = 0 // reset
            val conversationRepository = repository.getConversationRepository()
            if (conversationRepository != null) {
                emit(TokoChatResult.Loading)
                try {
                    // Get Local Data
                    val channels = conversationRepository.getAllCachedChannels(channelTypes)
                    var batchSize = defaultBatchSize
                    channels.asFlow().map {
                        TokoChatResult.Success(it)
                    }.collect {
                        if (it.data.size > batchSize) {
                            batchSize = it.data.size
                        }
                        this.emit(it) // Emit data from remote
                    }

                    // Get Remote Data
                    emitAll(fetchAllRemoteChannels(channelTypes, batchSize))
                } catch (throwable: Throwable) {
                    emit(TokoChatResult.Error(throwable))
                }
            } else {
                emit((TokoChatResult.Error(Throwable(ERROR_CONVERSATION_NULL))))
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
            // Set to -1 to mark as no more data
            lastTimeStamp = list.lastOrNull()?.createdAt ?: -1
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
