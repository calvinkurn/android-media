package com.tokopedia.chatbot.view.adapter.viewholder.listener

import com.tokopedia.chatbot.data.chatactionbubble.ChatActionBubbleViewModel
import com.tokopedia.chatbot.data.chatactionbubble.ChatActionSelectionBubbleViewModel

/**
 * @author by nisie on 10/12/18.
 */
interface ChatActionListBubbleListener {
    fun onChatActionBalloonSelected(selected: ChatActionBubbleViewModel,
                                    model: ChatActionSelectionBubbleViewModel)
}