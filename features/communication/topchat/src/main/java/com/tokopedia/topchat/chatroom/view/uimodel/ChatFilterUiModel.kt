package com.tokopedia.topchat.chatroom.view.uimodel

import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatlist.data.ChatListQueriesConstant.PARAM_FILTER_ALL
import com.tokopedia.topchat.chatroom.view.adapter.typefactory.ChatFilterTypeFactory

data class ChatFilterUiModel constructor(
        @StringRes
        val tagName: Int = R.string.filter_chat_all,
        val id: String = PARAM_FILTER_ALL
) : Visitable<ChatFilterTypeFactory> {
    override fun type(typeFactory: ChatFilterTypeFactory): Int {
        return typeFactory.type(this)
    }
}