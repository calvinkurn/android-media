package com.tokopedia.play.util.chat

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Created by kenny.hadisaputra on 23/05/22
 */
open class ChatStreams @AssistedInject constructor(
    @Assisted private val scope: CoroutineScope,
    private val dispatchers: CoroutineDispatchers,
) {

    @AssistedFactory
    interface Factory {
        fun create(scope: CoroutineScope): ChatStreams
    }

    private val mutex = Mutex()

    private val pendingChats = mutableListOf<PlayChatUiModel>()
    private val _chats = MutableStateFlow<List<PlayChatUiModel>>(emptyList())

    val chats: StateFlow<List<PlayChatUiModel>>
        get() = _chats

    private var isWaitingForHistory: Boolean = false

    init {
        setLoopingTimer()
    }

    open fun setWaitingForHistory() {
        isWaitingForHistory = true
    }

    open fun addChat(chat: PlayChatUiModel) {
        /**
         * When we get chat either from local user / web socket,
         * set isWaitingForHistory to false so the chat won't
         * be override by history later on.
         */
        isWaitingForHistory = false

        scope.launch(dispatchers.computation) {
            mutex.withLock {
                pendingChats.add(chat)
                if (chat.isSelfMessage) sendPendingChats()
            }
        }
    }

    fun addHistoryChat(chatList: List<PlayChatUiModel>) {
        if(isWaitingForHistory) {
            isWaitingForHistory = false

            scope.launch(dispatchers.computation) {
                mutex.withLock {
                    pendingChats.clear()
                    pendingChats.addAll(chatList)
                    sendPendingChats(isClearAllPrevChats = true)
                }
            }
        }
    }

    protected open fun setLoopingTimer() {
        scope.launch(dispatchers.computation) {
            while (isActive) {
                delay(DELAY_PER_STREAM)
                mutex.withLock { sendPendingChats() }
            }
        }
    }

    protected fun sendPendingChats(isClearAllPrevChats: Boolean = false) {
        if (pendingChats.isEmpty()) return

        _chats.update {
            if(isClearAllPrevChats)
                pendingChats.takeLast(MAX_CHAT)
            else
                it.takeLast((MAX_CHAT - pendingChats.size).coerceAtLeast(0)) + pendingChats
        }
        pendingChats.clear()
    }

    companion object {
        private const val MAX_CHAT = 30
        private const val DELAY_PER_STREAM = 500L
    }
}
