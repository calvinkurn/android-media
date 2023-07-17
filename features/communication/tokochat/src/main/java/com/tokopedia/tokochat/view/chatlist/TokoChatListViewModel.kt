package com.tokopedia.tokochat.view.chatlist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.channel.ConversationsChannel
import com.gojek.conversations.utils.ConversationsConstants
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.tokochat.domain.usecase.TokoChatChannelUseCase
import com.tokopedia.tokochat_common.util.TokoChatCacheManager
import com.tokopedia.tokochat_common.view.chatlist.uimodel.TokoChatListItemUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoChatListViewModel @Inject constructor(
    private val chatChannelUseCase: TokoChatChannelUseCase,
    private val mapper: TokoChatListUiMapper,
    private val cacheManager: TokoChatCacheManager,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _chatListData = MutableLiveData<Result<List<TokoChatListItemUiModel>>>()
    val chatListData: LiveData<Result<List<TokoChatListItemUiModel>>>
        get() = _chatListData

    private var _channelList: LiveData<List<ConversationsChannel>>? = null

    private val _error = MutableLiveData<Pair<Throwable, String>>()
    val error: LiveData<Pair<Throwable, String>>
        get() = _error

    fun getChatListFlow(): Flow<List<TokoChatListItemUiModel>> {
        var resultSize: Int = Int.ZERO
        return chatChannelUseCase.getAllCachedChannels(listOf(ChannelType.GroupBooking))
            .asFlow()
            .onStart {
                Log.d("TOKOCHAT-lIST", "START-LOADING")
                chatChannelUseCase.setLastTimeStamp(0L)
            }
            .map {
                resultSize = it.size
                val listUiModel = it.map { channel ->
                    mapper.mapToChatListItem(channel)
                }
                listUiModel
            }.onEach {
                _chatListData.value = Success(it)
                val batchSize = if (resultSize <= ConversationsConstants.DEFAULT_BATCH_SIZE) {
                    ConversationsConstants.DEFAULT_BATCH_SIZE
                } else {
                    resultSize
                }
                loadChatList(batchSize)
            }.catch {
                _error.value = Pair(it, ::getChatListFlow.name)
            }
    }

    fun loadChatList(batchSize: Int = ConversationsConstants.DEFAULT_BATCH_SIZE) {
        viewModelScope.launch {
            try {
                val result = arrayListOf<TokoChatListItemUiModel>()
                chatChannelUseCase.getAllChannel(
                    channelTypes = listOf(ChannelType.GroupBooking),
                    batchSize = batchSize,
                    onSuccess = {
                        val listUiModel = it.map { channel ->
                            mapper.mapToChatListItem(channel)
                        }
                        result.addAll(listUiModel)
                        // Set to -1 to mark as no more data
                        chatChannelUseCase.setLastTimeStamp(it.lastOrNull()?.createdAt ?: -1)
                        _chatListData.value = Success(result)
                    },
                    onError = {
                        it?.let { error ->
                            _error.value = Pair(error, ::loadChatList.name)
                        }
                    }
                )
            } catch (throwable: Throwable) {
                _error.value = Pair(throwable, ::loadChatList.name)
                _chatListData.value = Fail(throwable)
            }
        }
    }

    private fun setChannelList() {
        try {
            if (_channelList == null) {
                _channelList = chatChannelUseCase.getAllCachedChannels(
                    listOf(
                        ChannelType.GroupBooking
                    )
                )
            }
        } catch (throwable: Throwable) {
            _error.value = Pair(throwable, ::setChannelList.name)
        }
    }
}
