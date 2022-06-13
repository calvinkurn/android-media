package com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory

import android.view.View
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment.AttachmentPreviewViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment.InvoicePreviewViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment.ProductPreviewViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment.VoucherPreviewViewHolder
import com.tokopedia.topchat.chatroom.view.viewmodel.InvoicePreviewUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableVoucherPreview
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatProductAttachmentPreviewUiModel

class AttachmentPreviewFactoryImpl : AttachmentPreviewFactory {

    override fun type(productPreviewViewModel: InvoicePreviewUiModel): Int {
        return InvoicePreviewViewHolder.LAYOUT
    }

    override fun type(sendableVoucherPreview: SendableVoucherPreview): Int {
        return VoucherPreviewViewHolder.LAYOUT
    }

    override fun type(product: TopchatProductAttachmentPreviewUiModel): Int {
        return ProductPreviewViewHolder.LAYOUT
    }

    override fun create(
            viewType: Int,
            view: View,
            itemListener: AttachmentPreviewViewHolder.AttachmentItemPreviewListener
    ): AttachmentPreviewViewHolder<*> {
        return when (viewType) {
            ProductPreviewViewHolder.LAYOUT -> ProductPreviewViewHolder(view, itemListener)
            InvoicePreviewViewHolder.LAYOUT -> InvoicePreviewViewHolder(view, itemListener)
            VoucherPreviewViewHolder.LAYOUT -> VoucherPreviewViewHolder(view, itemListener)
            else -> throw IllegalStateException("Unknown View Type: $viewType")
        }
    }

}