package com.tokopedia.topchat.chatlist.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatlist.view.adapter.typefactory.ChatListTypeFactory

data class ChatListTickerUiModel(
    val message: String,
    val tickerType: Int,
): Visitable<ChatListTypeFactory> {
    override fun type(typeFactory: ChatListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
