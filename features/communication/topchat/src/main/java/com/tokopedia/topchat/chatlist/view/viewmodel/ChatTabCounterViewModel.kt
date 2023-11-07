package com.tokopedia.topchat.chatlist.view.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.topchat.chatlist.domain.pojo.NotificationsPojo
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
    private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _actionFlow =
        MutableSharedFlow<TopChatListAction>(extraBufferCapacity = 16)

    private val _chatListNotificationUiState = MutableStateFlow(
        TopChatListNotificationCounterUiState()
    )
    val chatListNotificationUiState = _chatListNotificationUiState.asStateFlow()

    private val _errorUiState = MutableSharedFlow<TopChatListErrorUiState>(
        extraBufferCapacity = 16
    )
    val errorUiState = _errorUiState.asSharedFlow()

    fun setupViewModelObserver() {
        _actionFlow.process()
        observeNotificationCounter()
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
                    updateCounter(isSellerTab = it.isSellerTab)
                }
            }
        }
            .flowOn(dispatcher.immediate)
            .launchIn(viewModelScope)
    }

    private fun observeNotificationCounter() {
        viewModelScope.launch {
            try {
                chatNotificationCounterUseCase.notificationsCounterFlow.collectLatest {
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

    private fun updateCounter(isSellerTab: Boolean) {
        Log.d("UPDATED-TAB", "$isSellerTab - ${chatListNotificationUiState.value}")
    }

    fun setLastVisitedTab(context: Context, position: Int) {
        context.getSharedPreferences(PREF_CHAT_LIST_TAB, Context.MODE_PRIVATE)
            .edit()
            .apply {
                putInt(KEY_LAST_POSITION, position)
                apply()
            }
    }

    fun getLastVisitedTab(context: Context): Int {
        return context.getSharedPreferences(PREF_CHAT_LIST_TAB, Context.MODE_PRIVATE)
            .getInt(KEY_LAST_POSITION, -1)
    }

    companion object {
        const val PREF_CHAT_LIST_TAB = "chatlist_tab_activity.pref"
        const val KEY_LAST_POSITION = "key_last_seen_tab_position"
    }
}
