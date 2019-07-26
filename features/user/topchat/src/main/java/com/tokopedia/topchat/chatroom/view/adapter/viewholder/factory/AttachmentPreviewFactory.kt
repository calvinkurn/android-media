package com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory

import android.view.View
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment.AttachmentPreviewViewHolder
import com.tokopedia.topchat.chatroom.view.viewmodel.InvoicePreviewViewModel
import com.tokopedia.topchat.chatroom.view.viewmodel.ProductPreviewViewModel

interface AttachmentPreviewFactory {

    fun type(productPreviewViewModel: ProductPreviewViewModel): Int

    fun type(productPreviewViewModel: InvoicePreviewViewModel): Int

    fun create(
            viewType: Int,
            view: View,
            itemListener: AttachmentPreviewViewHolder.AttachmentItemPreviewListener
    ): AttachmentPreviewViewHolder<*>

}