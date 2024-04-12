package com.tokopedia.topchat.chatroom.view.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.chat_common.data.BaseChatUiModel
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.topchat.chatroom.view.adapter.typefactory.TopChatRoomTypeFactory

class ProductCarouselUiModel constructor(
    var products: List<Visitable<*>>,
    val isSender: Boolean,
    messageId: String,
    fromUid: String?,
    from: String,
    fromRole: String,
    attachmentId: String,
    attachmentType: String,
    replyTime: String?,
    message: String,
    source: String
) : BaseChatUiModel(
    messageId, fromUid, from, fromRole,
    attachmentId, attachmentType, replyTime, message, source
),
    Visitable<TopChatRoomTypeFactory> {
    override fun type(typeFactory: TopChatRoomTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun isBroadCast(): Boolean {
        if (products.isEmpty()) return false
        val product = products.first()
        return product is ProductAttachmentUiModel && product.fromBroadcast()
    }

    // If one of the product is loading. all state become loading
    fun isLoading(): Boolean {
        var isLoading = false
        for (product in products) {
            if (product is ProductAttachmentUiModel) {
                if (product.isLoading) {
                    isLoading = true
                }
            }
        }
        return isLoading
    }

    companion object {
        fun mapToCarousel(
            listProductPreviewAttachment: List<Visitable<*>>
        ): ProductCarouselUiModel? {
            val product = listProductPreviewAttachment.firstOrNull()
            return if (product != null && product is ProductAttachmentUiModel) {
                ProductCarouselUiModel(
                    products = listProductPreviewAttachment,
                    isSender = product.isSender,
                    messageId = product.messageId,
                    fromUid = product.fromUid,
                    from = product.from,
                    fromRole = product.fromRole,
                    attachmentId = product.attachmentId,
                    attachmentType = product.attachmentType,
                    replyTime = product.replyTime,
                    message = product.message,
                    source = product.source
                )
            } else {
                null
            }
        }
    }
}
