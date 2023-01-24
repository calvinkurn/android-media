package com.tokopedia.topchat.chatlist.view.listener

import com.tokopedia.topchat.chatlist.view.uimodel.ChatListTickerUiModel

interface ChatListTickerListener {
    fun onChatListTickerClicked(applink: String, isForBubble: Boolean)
    fun onDismissTicker(element: ChatListTickerUiModel)
    fun onChatListTickerImpressed(isForBubble: Boolean)
}
