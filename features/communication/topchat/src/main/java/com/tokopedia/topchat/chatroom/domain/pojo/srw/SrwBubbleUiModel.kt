package com.tokopedia.topchat.chatroom.domain.pojo.srw

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.topchat.chatroom.view.adapter.typefactory.TopChatRoomTypeFactory
import com.tokopedia.topchat.chatroom.view.custom.SrwFrameLayout
import com.tokopedia.topchat.chatroom.view.uimodel.SendablePreview
import com.tokopedia.topchat.chatroom.view.uimodel.TopchatProductAttachmentPreviewUiModel

data class SrwBubbleUiModel constructor(
    val srwPreviewState: SrwFrameLayout.SrwState,
    val products: List<SendablePreview>
) : Visitable<TopChatRoomTypeFactory> {

    var isExpanded = true
    var alreadyInitializeWithSrwPreview = false

    val productIds: List<String>
        get() = products
            .filterIsInstance<TopchatProductAttachmentPreviewUiModel>()
            .map { it.productId }

    override fun type(typeFactory: TopChatRoomTypeFactory): Int {
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
