package com.tokopedia.topchat.chatroom.domain.pojo.srw

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory
import com.tokopedia.topchat.chatroom.view.custom.SrwLinearLayout

data class SrwBubbleUiModel(
        val srwHangingState: SrwLinearLayout.SrwState
) : Visitable<TopChatTypeFactory> {
    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }
}