package com.tokopedia.chatbot.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chatbot.view.adapter.ChatbotTypeFactory

class ConnectionDividerViewModel(val message: String?,val isShowButton:Boolean, val type: String, val leaveQueue: (() -> Unit)?) : Visitable<ChatbotTypeFactory> {

    override fun type(typeFactory: ChatbotTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun equals(other: Any?): Boolean {
        return other is ConnectionDividerViewModel
    }

}