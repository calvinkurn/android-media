package com.tokopedia.topchat.chatlist.model

/**
 * @author : Steven 2019-09-06
 */

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatlist.adapter.typefactory.ChatListTypeFactory

data class EmptyChatModel(
        var title: String = "",
        var body: String = "",
        var image: String = ""
) : Visitable<ChatListTypeFactory>{

    override fun type(typeFactory: ChatListTypeFactory): Int {
        return typeFactory.type(this)
    }
}