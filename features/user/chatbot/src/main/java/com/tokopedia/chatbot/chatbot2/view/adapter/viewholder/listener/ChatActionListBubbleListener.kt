package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener

import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionBubbleUiModel
import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionSelectionBubbleUiModel

/**
 * @author by nisie on 10/12/18.
 */
interface ChatActionListBubbleListener {
    fun onChatActionBalloonSelected(
        selected: ChatActionBubbleUiModel,
        model: ChatActionSelectionBubbleUiModel
    )
}
