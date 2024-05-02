package com.tokopedia.topchat.chatlist.view.viewmodel

import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatlist.domain.pojo.NotificationsPojo
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatListLastVisitedTabUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.GetChatNotificationCounterUseCase
import com.tokopedia.topchat.chatlist.view.TopChatListAction
import com.tokopedia.topchat.chatlist.view.uistate.TopChatListErrorUiState
import com.tokopedia.topchat.chatlist.view.uistate.TopChatListNotificationCounterUiState
import com.tokopedia.topchat.common.data.TopChatResult
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
import timber.log.Timber
import javax.inject.Inject

class ChatTabCounterViewModel @Inject constructor(
    private val chatNotificationCounterUseCase: GetChatNotificationCounterUseCase,
    private val chatLastVisitedTabUseCase: GetChatListLastVisitedTabUseCase,
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _actionFlow =
        MutableSharedFlow<TopChatListAction>(extraBufferCapacity = 16)

    private val _chatListNotificationUiState = MutableStateFlow(
        TopChatListNotificationCounterUiState()
    )
    val chatListNotificationUiState = _chatListNotificationUiState.asStateFlow()

    private val _chatLastSelectedTab = MutableStateFlow<TopChatResult<Int>>(
        TopChatResult.Loading
    )
    val chatLastSelectedTab = _chatLastSelectedTab.asStateFlow()

    private val _errorUiState = MutableSharedFlow<TopChatListErrorUiState>(
        extraBufferCapacity = 16
    )
    val errorUiState = _errorUiState.asSharedFlow()

    fun setupViewModelObserver() {
        _actionFlow.process()
        observeNotificationCounter()
        observeLastVisitedTab()
    }

    fun processAction(action: TopChatListAction) {
        viewModelScope.launch {
            _actionFlow.emit(action)
        }
    }

    private fun Flow<TopChatListAction>.process() {
        onEach {
            when (it) {
                is TopChatListAction.RefreshCounter -> {
                    refreshNotifCounter(shopId = it.shopId)
                }
                is TopChatListAction.UpdateCounter -> {
                    updateCounter(
                        isSellerTab = it.isSellerTab,
                        adjustableCounter = it.adjustableCounter
                    )
                }
                is TopChatListAction.SetLastVisitedTab -> {
                    setLastVisitedTab(it.position)
                }
                else -> Unit
            }
        }
            .flowOn(dispatcher.immediate)
            .launchIn(viewModelScope)
    }

    private fun observeNotificationCounter() {
        viewModelScope.launch {
            try {
                chatNotificationCounterUseCase.observe().collectLatest {
                    when (it) {
                        is TopChatResult.Success -> {
                            onSuccessRefreshNotification(it.data)
                        }
                        is TopChatResult.Error -> {
                            onErrorRefreshNotification(it.throwable)
                        }
                        TopChatResult.Loading -> {
                            setLoading()
                        }
                    }
                }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                _errorUiState.tryEmit(
                    TopChatListErrorUiState(
                        error = Pair(throwable, ::observeNotificationCounter.name)
                    )
                )
            }
        }
    }

    private fun refreshNotifCounter(shopId: String) {
        viewModelScope.launch {
            chatNotificationCounterUseCase.refreshCounter(shopId)
        }
    }

    private fun onSuccessRefreshNotification(notifications: NotificationsPojo) {
        _chatListNotificationUiState.update {
            it.copy(
                isLoading = false,
                unreadSeller = notifications.notification.chat.unreadsSeller,
                unreadBuyer = notifications.notification.chat.unreadsUser
            )
        }
    }

    private fun onErrorRefreshNotification(throwable: Throwable) {
        // Remove all numbers because it's not relevant and could be wrong
        resetNotificationCounterUiState()
        _errorUiState.tryEmit(
            TopChatListErrorUiState(
                error = Pair(throwable, ::onErrorRefreshNotification.name)
            )
        )
    }

    private fun resetNotificationCounterUiState() {
        _chatListNotificationUiState.update {
            it.copy(
                isLoading = false,
                unreadSeller = 0,
                unreadBuyer = 0
            )
        }
    }

    private fun setLoading() {
        _chatListNotificationUiState.update {
            it.copy(isLoading = true)
        }
    }

    private fun updateCounter(
        isSellerTab: Boolean,
        adjustableCounter: Int
    ) {
        if (isSellerTab) {
            _chatListNotificationUiState.update {
                it.copy(unreadSeller = it.unreadSeller + adjustableCounter)
            }
        } else {
            _chatListNotificationUiState.update {
                it.copy(unreadBuyer = it.unreadBuyer + adjustableCounter)
            }
        }
    }

    private fun observeLastVisitedTab() {
        viewModelScope.launch {
            chatLastVisitedTabUseCase.observeLastVisitedTab().collectLatest {
                _chatLastSelectedTab.value = TopChatResult.Success(it)
            }
        }
    }

    private fun setLastVisitedTab(position: Int) {
        viewModelScope.launch {
            try {
                chatLastVisitedTabUseCase.setLastVisitedTab(position)
            } catch (throwable: Throwable) {
                Timber.d(throwable)
                _errorUiState.tryEmit(
                    TopChatListErrorUiState(
                        Pair(
                            throwable,
                            ::setLastVisitedTab.name
                        )
                    )
                )
            }
        }
    }
}
