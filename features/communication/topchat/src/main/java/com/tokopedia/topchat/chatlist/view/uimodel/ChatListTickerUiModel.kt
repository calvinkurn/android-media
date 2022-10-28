package com.tokopedia.topchat.chatlist.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatlist.view.adapter.typefactory.ChatListTypeFactory

data class ChatListTickerUiModel(
    var message: String,
    val tickerType: Int,
    val appLink: String
): Visitable<ChatListTypeFactory> {

    var showCloseButton = false

    override fun type(typeFactory: ChatListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
