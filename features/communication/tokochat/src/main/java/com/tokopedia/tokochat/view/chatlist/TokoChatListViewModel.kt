package com.tokopedia.tokochat.view.chatlist

import androidx.lifecycle.viewModelScope
import com.gojek.conversations.babble.channel.data.ChannelType
import com.gojek.conversations.channel.ConversationsChannel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.tokochat.common.util.TokoChatCommonValueUtil.BATCH_LIMIT
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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

    private val _chatListTrackerUiState = MutableSharedFlow<Map<String, Int>>()
    val chatListTrackerUiState = _chatListTrackerUiState.asSharedFlow()

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
    private var hasFetchFirstPageLocal = false

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
                is TokoChatListAction.NavigateToPage -> {
                    navigateToPage(it.applink)
                }
                TokoChatListAction.RefreshPage -> {
                    resetChatListData()
                    observeChatListItemFlow()
                }
                TokoChatListAction.LoadNextPage -> {
                    loadNextPageChatList(BATCH_LIMIT)
                }
            }
        }
            .launchIn(viewModelScope)
    }

    private fun observeChatListItemFlow() {
        // Cancel existing job first & mark has not load local first page
        val previousJob = chatListJob
        previousJob?.cancel()
        hasFetchFirstPageLocal = false
        chatListJob = viewModelScope.launch(dispatcher.io) {
            try {
                previousJob?.join() // Wait until previous job finished
                chatListUseCase.fetchAllCachedChannels(
                    listOf(ChannelType.GroupBooking)
                ).collectLatest { result ->
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
        if (!hasFetchFirstPageLocal) {
            // If first time load from local, fetch newest data from remote
            loadNextPageChatList(channelList.size)
            hasFetchFirstPageLocal = true
        } else {
            val filteredChannelList = filterExpiredChannel(channelList)
            val chatItemList = mapper.mapToListChat(filteredChannelList)
            _chatListUiState.update {
                it.copy(
                    isLoading = false,
                    chatItemList = chatItemList,
                    errorMessage = null
                )
            }
        }
    }

    private fun loadNextPageChatList(batchSize: Int) {
        viewModelScope.launch(dispatcher.io) {
            try {
                _chatListUiState.update {
                    it.copy(page = it.page + 1)
                }
                chatListUseCase.fetchAllRemoteChannels(
                    channelTypes = listOf(ChannelType.GroupBooking),
                    batchSize = getBatchSize(batchSize)
                ).collectLatest { result ->
                    if (result is TokoChatResult.Error) {
                        _chatListUiState.update {
                            it.copy(
                                errorMessage = result.throwable.message,
                                isLoading = false
                            )
                        }
                    } else if (result is TokoChatResult.Success) {
                        // Track if first page
                        if (chatListUiState.value.page == 1) {
                            val trackerData = mapper.mapToTypeCounter(
                                filterExpiredChannel(result.data)
                            )
                            _chatListTrackerUiState.emit(trackerData)
                        }

                        // Set hasNextPage based on remote data
                        _chatListUiState.update { currentState ->
                            val hasNextPage = result.data.isNotEmpty()
                            currentState.copy(
                                hasNextPage = hasNextPage,
                                isLoading = hasNextPage // The isLoading flag is set to false when hasNextPage is false, loading should be stopped when there's no next page
                            )
                        }
                    }
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

    private fun filterExpiredChannel(
        channelList: List<ConversationsChannel>
    ): List<ConversationsChannel> {
        return channelList.filter {
            it.expiresAt > System.currentTimeMillis() || it.expiresAt == 0L // Expires At should not be 0, If 0 then SDK initialize the chat and the data is still being fetch
        }
    }

    private fun resetChatListData() {
        _chatListUiState.update {
            it.copy(
                isLoading = false,
                chatItemList = listOf(),
                page = 0,
                hasNextPage = true,
                errorMessage = null
            )
        }
    }

    private fun navigateToPage(applink: String) {
        viewModelScope.launch {
            _navigationUiState.emit(
                TokoChatListNavigationUiState(applink = applink)
            )
        }
    }
}
