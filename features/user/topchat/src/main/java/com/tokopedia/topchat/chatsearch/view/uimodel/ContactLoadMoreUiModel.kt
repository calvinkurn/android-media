package com.tokopedia.topchat.chatsearch.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactory

data class ContactLoadMoreUiModel(
        val totalCount: String = "",
        val hideCta: Boolean = false
) : Visitable<ChatSearchTypeFactory> {

    override fun type(typeFactory: ChatSearchTypeFactory): Int {
        return typeFactory.type(this)
    }

}