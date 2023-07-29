package com.tokopedia.tokochat.view.chatlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokochat.common.util.TokoChatValueUtil.BATCH_LIMIT
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel
import com.tokopedia.tokochat.domain.usecase.TokoChatChannelUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transformLatest
import timber.log.Timber
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoChatListViewModel @Inject constructor(
    private val chatChannelUseCase: TokoChatChannelUseCase,
    private val mapper: TokoChatListUiMapper,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _chatListPair = MutableLiveData<Map<String, Int>>()
    val chatListPair: LiveData<Map<String, Int>>
        get() = _chatListPair

    private val _error = MutableLiveData<Pair<Throwable, String>>()
    val error: LiveData<Pair<Throwable, String>>
        get() = _error

    fun getChatListFlow(): Flow<Result<List<TokoChatListItemUiModel>>>? {
        return try {
            chatChannelUseCase.getAllCachedChannels(listOf(ChannelType.GroupBooking))
                ?.onStart {
                    setPaginationTimeStamp(0L) // reset
                }
                ?.map {
                    filterExpiredChannelAndMap(it)
                }
                ?.transformLatest { value ->
                    emit(Success(value) as Result<List<TokoChatListItemUiModel>>)
                }
                ?.catch {
                    _error.value = Pair(it, ::getChatListFlow.name)
                    emit(Fail(it))
                }
                ?.flowOn(dispatcher.io)
        } catch (throwable: Throwable) {
            Timber.d(throwable)
            flow {
                emit(Fail(throwable))
            }
        }
    }

    fun loadNextPageChatList(
        localSize: Int = Int.ZERO,
        isLoadMore: Boolean,
        onCompleted: (Boolean) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                chatChannelUseCase.getAllChannel(
                    channelTypes = listOf(ChannelType.GroupBooking),
                    batchSize = getBatchSize(localSize),
                    onSuccess = {
                        // Set to -1 to mark as no more data
                        setPaginationTimeStamp(it.lastOrNull()?.createdAt ?: -1)
                        if (!isLoadMore) {
                            emitChatListPairData(channelList = it)
                        }
                    },
                    onError = {
                        it?.let { error ->
                            _error.value = Pair(error, ::loadNextPageChatList.name)
                        }
                        onCompleted(false)
                    }
                )
            } catch (throwable: Throwable) {
                _error.value = Pair(throwable, ::loadNextPageChatList.name)
            }
        }
    }

    private fun getBatchSize(localSize: Int): Int {
        return if (localSize <= BATCH_LIMIT) {
            BATCH_LIMIT
        } else {
            localSize
        }
    }

    private fun setPaginationTimeStamp(newTimeStamp: Long) {
        chatChannelUseCase.setLastTimeStamp(newTimeStamp)
    }

    private fun emitChatListPairData(channelList: List<ConversationsChannel>) {
        _chatListPair.value = mapper.mapToTypeCounter(channelList)
    }

    private fun filterExpiredChannelAndMap(
        channelList: List<ConversationsChannel>
    ): List<TokoChatListItemUiModel> {
        val filteredChannel = channelList.filter {
            it.expiresAt > System.currentTimeMillis()
        }
        return mapper.mapToListChat(filteredChannel)
    }
}
