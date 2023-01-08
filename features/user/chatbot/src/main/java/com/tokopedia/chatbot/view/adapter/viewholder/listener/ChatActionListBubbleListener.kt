package com.tokopedia.chatbot.view.adapter.viewholder.listener

import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleUiModel

/**
 * @author by nisie on 10/12/18.
 */
interface ChatActionListBubbleListener {
    fun onChatActionBalloonSelected(
        selected: ChatActionBubbleUiModel,
        model: ChatActionSelectionBubbleUiModel
    )
}
