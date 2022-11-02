package com.tokopedia.play.fake.chat

import com.tokopedia.play.util.chat.ChatManager
import com.tokopedia.play.util.chat.ChatStreams
import com.tokopedia.play_common.model.ui.PlayChatUiModel

/**
 * Created By : Jonathan Darwin on October 27, 2022
 */
class FakeChatManager(
    chatStreams: FakeChatStreams,
) : ChatManager(chatStreams) {

    private var onIntercept: (() -> Unit)? = null

    override fun setWaitingForHistory() {
        super.setWaitingForHistory()
        onIntercept?.invoke()
    }

    fun interceptWaitingForHistory(onIntercept: () -> Unit) {
        this.onIntercept = onIntercept
    }
}
