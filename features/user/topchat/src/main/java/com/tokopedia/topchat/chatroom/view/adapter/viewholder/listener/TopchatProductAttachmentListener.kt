package com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener

import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer

interface TopchatProductAttachmentListener : ProductAttachmentListener {
    fun updateProductStock(
        product: ProductAttachmentUiModel,
        adapterPosition: Int,
        parentMetaData: SingleProductAttachmentContainer.ParentViewHolderMetaData?
    )

    fun trackClickUpdateStock(product: ProductAttachmentUiModel)
    fun isOCCActive(): Boolean
}