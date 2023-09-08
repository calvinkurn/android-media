package com.tokopedia.inbox.universalinbox.domain.usecase

import androidx.lifecycle.asFlow
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokochat.config.repository.TokoChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber
import javax.inject.Inject
import com.tokopedia.inbox.universalinbox.util.Result as Result

open class UniversalInboxGetAllDriverChannelsUseCase @Inject constructor(
    private val repository: TokoChatRepository,
    private val dispatchers: CoroutineDispatchers
) {

    private val channelFlow = MutableStateFlow<Result<List<ConversationsChannel>>>(
        Result.Loading
    )

    fun observe(): Flow<Result<List<ConversationsChannel>>> = channelFlow

    suspend fun observeDriverChannelFlow() {
        try {
            repository.getConversationRepository()?.getAllChannels(
                listOf(ChannelType.GroupBooking)
            )!!
                // Safe !! because of try catch
                // Should throw error when SDK repository is fail to init
                .asFlow()
                .flowOn(dispatchers.io)
                .collectLatest {
                    channelFlow.emit(Result.Success(it))
                }
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            channelFlow.emit(Result.Error(throwable))
        }
    }
}
