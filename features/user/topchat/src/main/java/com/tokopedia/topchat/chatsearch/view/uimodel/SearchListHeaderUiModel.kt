package com.tokopedia.topchat.chatsearch.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatsearch.view.adapter.ChatSearchTypeFactory

data class SearchListHeaderUiModel(
        val title: String = "",
        val totalCount: String = "",
        val hideCta: Boolean = false
) : Visitable<ChatSearchTypeFactory> {

    override fun type(typeFactory: ChatSearchTypeFactory): Int {
        return typeFactory.type(this)
    }

    companion object {
        const val TITLE_CONTACT = "Pengguna"
        const val TITLE_REPLY = "Pesan"
    }
}