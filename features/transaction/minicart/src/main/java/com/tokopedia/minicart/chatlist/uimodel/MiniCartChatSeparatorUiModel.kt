package com.tokopedia.minicart.chatlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.chatlist.adapter.MiniCartChatListAdapterTypeFactory

data class MiniCartChatSeparatorUiModel(
    var height: Int = 0
) : Visitable<MiniCartChatListAdapterTypeFactory> {

    override fun type(typeFactory: MiniCartChatListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
