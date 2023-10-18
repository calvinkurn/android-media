package com.tokopedia.minicart.chatlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.chatlist.adapter.MiniCartChatListAdapterTypeFactory

class MiniCartChatUnavailableReasonUiModel(
    var reason: String = ""
) : Visitable<MiniCartChatListAdapterTypeFactory> {

    override fun type(typeFactory: MiniCartChatListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
