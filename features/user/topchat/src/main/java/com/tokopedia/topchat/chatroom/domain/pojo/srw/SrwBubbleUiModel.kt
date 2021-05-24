package com.tokopedia.topchat.chatroom.domain.pojo.srw

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory
import com.tokopedia.topchat.chatroom.view.custom.SrwFrameLayout
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview

data class SrwBubbleUiModel(
        val srwHangingState: SrwFrameLayout.SrwState,
        val products: List<SendablePreview>
) : Visitable<TopChatTypeFactory> {
    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }
}