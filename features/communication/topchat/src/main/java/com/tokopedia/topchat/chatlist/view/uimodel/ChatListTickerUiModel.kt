package com.tokopedia.topchat.chatlist.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topchat.chatlist.view.adapter.typefactory.ChatListTypeFactory

data class ChatListTickerUiModel(
    var message: String = "",
    var tickerType: Int = Int.ZERO,
    var applink: String = ""
) : Visitable<ChatListTypeFactory> {

    val impressHolder = ImpressHolder()

    var showCloseButton: Boolean = false
    var sharedPreferenceKey: String = ""

    override fun type(typeFactory: ChatListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
