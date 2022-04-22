package com.tokopedia.oldminicart.chatlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.oldminicart.chatlist.adapter.MiniCartChatListAdapterTypeFactory

data class MiniCartChatSeparatorUiModel(
        var height: Int = 0
) : Visitable<MiniCartChatListAdapterTypeFactory> {

    override fun type(typeFactory: MiniCartChatListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}