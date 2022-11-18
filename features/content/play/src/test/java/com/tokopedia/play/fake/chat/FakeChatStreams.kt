package com.tokopedia.play.fake.chat

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.util.chat.ChatStreams
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import kotlinx.coroutines.CoroutineScope

/**
 * Created By : Jonathan Darwin on October 20, 2022
 */
class FakeChatStreams(
    scope: CoroutineScope,
    dispatchers: CoroutineDispatchers,
) : ChatStreams(scope, dispatchers) {

    private var onIntercept: (() -> Unit)? = null

    override fun setLoopingTimer() {
        /**
         * To make ChatStreams testable
         * we need to get rid of infinite while inside
         * coroutine scope, so the dispatcher can be idle and
         * can proceed to next step of test
         */
    }

    override fun addChat(chat: PlayChatUiModel) {
        super.addChat(chat)
        sendPendingChats()
    }
}
