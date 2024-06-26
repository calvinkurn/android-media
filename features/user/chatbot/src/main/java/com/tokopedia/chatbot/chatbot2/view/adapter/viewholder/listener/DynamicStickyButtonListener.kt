package com.tokopedia.chatbot.chatbot2.view.adapter.viewholder.listener

import com.tokopedia.chatbot.chatbot2.view.uimodel.chatactionbubble.ChatActionBubbleUiModel

interface DynamicStickyButtonListener {

    fun onValidateCtaVisibility()
    fun onButtonActionClicked(bubble: ChatActionBubbleUiModel)
}
