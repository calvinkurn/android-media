package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder

import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer

object TopchatProductAttachmentViewHolderBinder {
    fun bindNewOccState(
            element: ProductAttachmentViewModel,
            productView: SingleProductAttachmentContainer?
    ) {
        productView?.bindOcc(element)
    }
}