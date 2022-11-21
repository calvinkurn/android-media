package com.tokopedia.play.util.chat

import com.tokopedia.play_common.model.ui.PlayChatUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.StateFlow

/**
 * Created By : Jonathan Darwin on October 27, 2022
 */
open class ChatManager @AssistedInject constructor(
    @Assisted private val chatStreams: ChatStreams,
) {
    @AssistedFactory
    interface Factory {
        fun create(chatStreams: ChatStreams): ChatManager
    }

    val chats: StateFlow<List<PlayChatUiModel>>
        get() = chatStreams.chats

    private var isWaitingForHistory: Boolean = false

    open fun setWaitingForHistory() {
        isWaitingForHistory = true
    }

    fun addChat(chat: PlayChatUiModel) {
        /**
         * When we get chat either from local user / web socket,
         * set isWaitingForHistory to false so the chat won't
         * be override by history later on.
         */
        isWaitingForHistory = false

        chatStreams.addChat(chat)
    }

    fun addHistoryChat(chatList: List<PlayChatUiModel>) {
        if(isWaitingForHistory) {
            isWaitingForHistory = false
            chatStreams.addHistoryChat(chatList)
        }
    }
}
