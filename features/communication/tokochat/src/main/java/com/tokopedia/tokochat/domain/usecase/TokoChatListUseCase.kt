package com.tokopedia.tokochat.domain.usecase

import androidx.lifecycle.asFlow
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.channel.GetChannelRequest
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import com.tokopedia.tokochat.config.util.TokoChatResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoChatListUseCase @Inject constructor(
    private val repository: TokoChatRepository,
    dispatchers: CoroutineDispatchers
) {

    private val _conversationChannelFlow = MutableStateFlow<TokoChatResult<List<ConversationsChannel>>>(
        TokoChatResult.Loading
    )
    val conversationChannelFlow = _conversationChannelFlow.asStateFlow()

    private val supervisorJob = SupervisorJob()
    private val scope = CoroutineScope(dispatchers.io + supervisorJob)

    private var lastTimeStamp: Long = 0

    fun fetchAllChannel(
        channelTypes: List<ChannelType>,
        batchSize: Int
    ) {
        if (lastTimeStamp < 0L) return
        repository.getConversationRepository()?.getAllChannels(
            getChannelRequest = GetChannelRequest(
                types = channelTypes,
                timestamp = getLastTimeStamp(),
                batchSize = batchSize
            ),
            onSuccess = {
                scope.launch {
                    // Set to -1 to mark as no more data
                    lastTimeStamp = it.lastOrNull()?.createdAt ?: -1
                    _conversationChannelFlow.emit(TokoChatResult.Success(it))
                }
            },
            onError = {
                scope.launch {
                    _conversationChannelFlow.emit(
                        TokoChatResult.Error(
                            it ?: MessageErrorException(
                                ERROR_FETCH_CHANNELS
                            )
                        )
                    )
                }
            }
        )
    }

    private fun getLastTimeStamp(): Long {
        return if (lastTimeStamp == 0L) {
            System.currentTimeMillis()
        } else {
            lastTimeStamp
        }
    }

    suspend fun fetchAllCachedChannels(
        channelTypes: List<ChannelType>
    ) {
        lastTimeStamp = 0 // reset
        repository.getConversationRepository()
            ?.getAllCachedChannels(channelTypes)
            ?.asFlow()
            ?.map { TokoChatResult.Success(it) }
            ?.catch { TokoChatResult.Error(it) }
            ?.collectLatest {
                _conversationChannelFlow.emit(it)
            }
    }

    fun cancel() {
        scope.cancel()
    }

    companion object {
        private const val ERROR_FETCH_CHANNELS = "Error fetch all channels"
    }
}
