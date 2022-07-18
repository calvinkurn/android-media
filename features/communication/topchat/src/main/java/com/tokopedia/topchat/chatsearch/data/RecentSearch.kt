package com.tokopedia.topchat.chatsearch.data

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactory

class RecentSearch: Visitable<ChatSearchTypeFactory> {
    override fun type(typeFactory: ChatSearchTypeFactory): Int {
        return typeFactory.type(this)
    }
}