package com.tokopedia.topchat.chatroom.domain.pojo.srw

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory
import com.tokopedia.topchat.chatroom.view.custom.SrwFrameLayout
import com.tokopedia.topchat.chatroom.view.viewmodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview

data class SrwBubbleUiModel constructor(
    val srwPreviewState: SrwFrameLayout.SrwState,
    val products: List<SendablePreview>
) : Visitable<TopChatTypeFactory> {

    var isExpanded = true
    var alreadyInitializeWithSrwPreview = false

    val productIds: List<String>
        get() = products
            .filterIsInstance<SendableProductPreview>()
            .map { it.productId }

    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun isRelatedTo(productId: String): Boolean {
        return productIds.contains(productId)
    }

    fun initializeWithSrwPreview() {
        if (!alreadyInitializeWithSrwPreview) {
            isExpanded = srwPreviewState.isExpanded
            alreadyInitializeWithSrwPreview = true
        }
    }
}