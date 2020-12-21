package com.tokopedia.topchat.chatroom.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatViewModel
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.topchat.chatroom.view.adapter.TopChatTypeFactory

class ProductCarouselUiModel constructor(
        var products: List<Visitable<*>>, val isSender: Boolean,
        messageId: String, fromUid: String?, from: String, fromRole: String,
        attachmentId: String, attachmentType: String, replyTime: String?, message: String,
        source: String
) : BaseChatViewModel(
        messageId, fromUid, from, fromRole,
        attachmentId, attachmentType, replyTime, message, source
), Visitable<TopChatTypeFactory> {
    override fun type(typeFactory: TopChatTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun isBroadCast(): Boolean {
        if (products.isEmpty()) return false
        val product = products.first()
        return product is ProductAttachmentViewModel && product.fromBroadcast()
    }

    // If one of the product is loading. all state become loading
    fun isLoading(): Boolean {
        var isLoading = false
        for (product in products) {
            if (product is ProductAttachmentViewModel) {
                if (product.isLoading) {
                    isLoading = true
                }
            }
        }
        return isLoading
    }
}