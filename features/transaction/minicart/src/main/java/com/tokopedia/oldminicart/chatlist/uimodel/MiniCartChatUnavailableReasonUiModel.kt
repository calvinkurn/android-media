package com.tokopedia.oldminicart.chatlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.oldminicart.chatlist.adapter.MiniCartChatListAdapterTypeFactory

class MiniCartChatUnavailableReasonUiModel (
    var reason: String = "",
) : Visitable<MiniCartChatListAdapterTypeFactory> {

    override fun type(typeFactory: MiniCartChatListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}