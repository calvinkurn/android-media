package com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener

import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer

interface TopchatProductAttachmentListener : ProductAttachmentListener {
    fun updateProductStock(
            product: ProductAttachmentViewModel,
            adapterPosition: Int,
            parentMetaData: SingleProductAttachmentContainer.ParentViewHolderMetaData?
    )

    fun trackClickUpdateStock(product: ProductAttachmentViewModel)
}