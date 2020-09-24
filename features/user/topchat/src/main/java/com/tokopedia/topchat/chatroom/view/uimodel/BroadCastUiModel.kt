package com.tokopedia.topchat.chatroom.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class BroadCastUiModel : Visitable<TopChatTypeFactory> {
    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }
}