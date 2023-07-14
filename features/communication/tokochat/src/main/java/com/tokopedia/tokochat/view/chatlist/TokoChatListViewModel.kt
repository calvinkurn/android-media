package com.tokopedia.tokochat.view.chatlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokochat.domain.usecase.TokoChatChannelUseCase
import com.tokopedia.tokochat_common.util.TokoChatCacheManager
import com.tokopedia.tokochat_common.view.chatlist.uimodel.TokoChatListItemUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch
import javax.inject.Inject

class TokoChatListViewModel @Inject constructor(
    private val chatChannelUseCase: TokoChatChannelUseCase,
    private val mapper: TokoChatListUiMapper,
    private val cacheManager: TokoChatCacheManager,
    private val dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main) {

    private val _chatListData = MutableLiveData<Result<List<TokoChatListItemUiModel>>>()
    val chatListData: LiveData<Result<List<TokoChatListItemUiModel>>>
        get() = _chatListData

    private var _channelList: LiveData<List<ConversationsChannel>>? = null

    private val _error = MutableLiveData<Pair<Throwable, String>>()
    val error: LiveData<Pair<Throwable, String>>
        get() = _error

    fun loadChatList() {
        viewModelScope.launch {
            try {
                val result = arrayListOf<TokoChatListItemUiModel>()
                chatChannelUseCase.getAllChannel(
                    channelTypes = listOf(ChannelType.GroupBooking),
                    onSuccess = {
                        it.forEach { channel ->
                            result.add(mapper.mapToChatListItem(channel))
                        }
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

    fun allCachedChannels(): LiveData<List<TokoChatListItemUiModel>> {
        setChannelList()
        val liveData = MutableLiveData<List<TokoChatListItemUiModel>>()
        viewModelScope.launch {
            try {
                val result = arrayListOf<TokoChatListItemUiModel>()
                _channelList?.value?.forEach {
                    result.add(mapper.mapToChatListItem(it))
                }
                liveData.value = result
            } catch (throwable: Throwable) {
                _error.value = Pair(throwable, ::allCachedChannels.name)
            }
        }
        return liveData
    }

    private fun setChannelList() {
        try {
            if (_channelList == null) {
                _channelList = chatChannelUseCase.getAllCachedChannels(listOf(
                    ChannelType.GroupBooking
                ))
            }
        } catch (throwable: Throwable) {
            _error.value = Pair(throwable, ::setChannelList.name)
        }
    }
}

