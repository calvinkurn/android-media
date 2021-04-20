package com.tokopedia.topchat.chatlist.pojo

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatlist.adapter.typefactory.ChatListTypeFactory

object ChatAdminNoAccessUiModel: Visitable<ChatListTypeFactory> {

    override fun type(typeFactory: ChatListTypeFactory): Int =
            typeFactory.type(this)

}