package com.tokopedia.tokochat.view.chatlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.distinctUntilChanged
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
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    private val _chatList:
        MediatorLiveData<Result<List<TokoChatListItemUiModel>>> = MediatorLiveData()
    val chatList: LiveData<Result<List<TokoChatListItemUiModel>>>
        get() = _chatList.distinctUntilChanged()

    fun setupChatListSource() {
        viewModelScope.launch {
            try {
                setPaginationTimeStamp(0L) // reset
                val cachedChannels = chatChannelUseCase.getAllCachedChannels(listOf(ChannelType.GroupBooking))
                _chatList.addSource(cachedChannels!!) { // expected to use !!
                    _chatList.value = Success(filterExpiredChannelAndMap(it))
                }
            } catch (throwable: Throwable) {
                _error.value = Pair(throwable, ::setupChatListSource.name)
                _chatList.value = Fail(throwable)
            }
        }
    }

    fun loadNextPageChatList(
        localSize: Int = Int.ZERO,
        isLoadMore: Boolean,
        onCompleted: (Pair<Boolean, Int?>) -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                withContext(dispatcher.io) {
                    chatChannelUseCase.getAllChannel(
                        channelTypes = listOf(ChannelType.GroupBooking),
                        batchSize = getBatchSize(localSize),
                        onSuccess = {
                            // Set to -1 to mark as no more data
                            setPaginationTimeStamp(it.lastOrNull()?.createdAt ?: -1)
                            if (!isLoadMore) {
                                emitChatListPairData(channelList = it)
                            }
                            onCompleted(Pair(true, it.size))
                        },
                        onError = {
                            it?.let { error ->
                                _error.postValue(Pair(error, ::loadNextPageChatList.name))
                            }
                            onCompleted(Pair(false, null))
                        }
                    )
                }
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
        _chatListPair.postValue(mapper.mapToTypeCounter(channelList))
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
