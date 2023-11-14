package com.tokopedia.tokochat.view.chatlist

import android.content.Intent
import androidx.lifecycle.viewModelScope
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokochat.common.util.TokoChatCommonValueUtil.BATCH_LIMIT
import com.tokopedia.tokochat.common.view.chatlist.uimodel.TokoChatListItemUiModel
import com.tokopedia.tokochat.config.util.TokoChatResult
import com.tokopedia.tokochat.domain.usecase.TokoChatListUseCase
import com.tokopedia.tokochat.view.chatlist.uistate.TokoChatListErrorUiState
import com.tokopedia.tokochat.view.chatlist.uistate.TokoChatListNavigationUiState
import com.tokopedia.tokochat.view.chatlist.uistate.TokoChatListUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokoChatListViewModel @Inject constructor(
    private val chatListUseCase: TokoChatListUseCase,
    private val mapper: TokoChatListUiMapper,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _actionFlow =
        MutableSharedFlow<TokoChatListAction>(extraBufferCapacity = 16)

    private val _chatListUiState = MutableStateFlow(TokoChatListUiState())
    val chatListUiState = _chatListUiState.asStateFlow()

    private val _errorUiState = MutableSharedFlow<TokoChatListErrorUiState>(
        extraBufferCapacity = 16
    )
    val errorUiState = _errorUiState.asSharedFlow()

    private val _navigationUiState = MutableSharedFlow<TokoChatListNavigationUiState>(
        extraBufferCapacity = 16,
        replay = 0
    )
    val navigationUiState = _navigationUiState.asSharedFlow()

    private var chatListJob: Job? = null

    fun setupViewModelObserver() {
        _actionFlow.process()
    }

    fun processAction(action: TokoChatListAction) {
        viewModelScope.launch {
            _actionFlow.emit(action)
        }
    }

    private fun Flow<TokoChatListAction>.process() {
        onEach {
            when (it) {
                is TokoChatListAction.NavigateWithIntent -> {
                    navigateWithIntent(it.intent)
                }
                is TokoChatListAction.NavigateToPage -> {
                    navigateToPage(it.applink)
                }
                TokoChatListAction.RefreshPage -> {
                    resetChatListData()
                    observeChatListItemFlow()
                    fetchLocalChatList()
                }
                TokoChatListAction.LoadNextPage -> {
                    loadNextPageChatList(BATCH_LIMIT)
                }
            }
        }
            .flowOn(dispatcher.immediate)
            .launchIn(viewModelScope)
    }

    private fun observeChatListItemFlow() {
        // Cancel existing job first
        val previousJob = chatListJob
        previousJob?.cancel()

        chatListJob = viewModelScope.launch {
            try {
                previousJob?.join() // Wait until previous job finished
                chatListUseCase.conversationChannelFlow
                    .collectLatest { result ->
                        when (result) {
                            is TokoChatResult.Success -> {
                                onSuccessGetChatItemList(result.data)
                            }
                            is TokoChatResult.Error -> {
                                _chatListUiState.update {
                                    it.copy(errorMessage = result.throwable.message.toString())
                                }
                            }
                            TokoChatResult.Loading -> {
                                _chatListUiState.update {
                                    it.copy(isLoading = true)
                                }
                            }
                        }
                    }
            } catch (throwable: Throwable) {
                val errorPair = Pair(throwable, ::observeChatListItemFlow.name)
                _errorUiState.emit(TokoChatListErrorUiState(errorPair))
            }
        }
    }

    private fun onSuccessGetChatItemList(channelList: List<ConversationsChannel>) {
        val filteredChatItemList = filterExpiredChannelAndMap(channelList)
        val trackerData = if (_chatListUiState.value.page == 0 &&
            _chatListUiState.value.trackerData == null
        ) {
            mapper.mapToTypeCounter(channelList)
        } else {
            null
        }
        // If local data is not flagged, then this is first load from local DB
        if (!_chatListUiState.value.localListLoaded) {
            loadNextPageChatList(channelList.size) // Load the page, then set as true
        }
        _chatListUiState.update {
            it.copy(
                isLoading = false,
                chatItemList = it.chatItemList + filteredChatItemList,
                trackerData = trackerData,
                errorMessage = null,
                hasNextPage = it.chatItemList.size <= filteredChatItemList.size,
                localListLoaded = true
            )
        }
    }

    private fun fetchLocalChatList() {
        viewModelScope.launch {
            try {
                chatListUseCase.fetchAllCachedChannels(listOf(ChannelType.GroupBooking))
            } catch (throwable: Throwable) {
                val errorPair = Pair(throwable, ::fetchLocalChatList.name)
                _errorUiState.emit(TokoChatListErrorUiState(errorPair))
            }
        }
    }

    private fun loadNextPageChatList(batchSize: Int) {
        viewModelScope.launch {
            try {
                _chatListUiState.update {
                    it.copy(page = it.page + 1)
                }
                withContext(dispatcher.io) {
                    chatListUseCase.fetchAllChannel(
                        channelTypes = listOf(ChannelType.GroupBooking),
                        batchSize = getBatchSize(batchSize)
                    )
                }
            } catch (throwable: Throwable) {
                val errorPair = Pair(throwable, ::loadNextPageChatList.name)
                _errorUiState.emit(TokoChatListErrorUiState(errorPair))
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

    private fun filterExpiredChannelAndMap(
        channelList: List<ConversationsChannel>
    ): List<TokoChatListItemUiModel> {
        val filteredChannel = channelList.filter {
            it.expiresAt > System.currentTimeMillis()
        }
        return mapper.mapToListChat(filteredChannel)
    }

    private fun resetChatListData() {
        _chatListUiState.update {
            it.copy(
                isLoading = false,
                chatItemList = listOf(),
                page = 0,
                hasNextPage = false,
                errorMessage = null,
                trackerData = null,
                localListLoaded = false
            )
        }
    }

    private fun navigateWithIntent(intent: Intent) {
        viewModelScope.launch {
            _navigationUiState.emit(
                TokoChatListNavigationUiState(
                    intent = intent,
                    applink = ""
                )
            )
        }
    }

    private fun navigateToPage(applink: String) {
        viewModelScope.launch {
            _navigationUiState.emit(
                TokoChatListNavigationUiState(
                    intent = null,
                    applink = applink
                )
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        chatListJob?.cancel()
        chatListUseCase.cancel()
    }
}
