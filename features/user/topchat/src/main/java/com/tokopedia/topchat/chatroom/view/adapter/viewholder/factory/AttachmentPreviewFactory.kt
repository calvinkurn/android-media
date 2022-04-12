package com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory

import android.view.View
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment.AttachmentPreviewViewHolder
import com.tokopedia.topchat.chatroom.view.viewmodel.InvoicePreviewUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableVoucherPreview
import com.tokopedia.topchat.chatroom.view.viewmodel.TopchatProductAttachmentPreviewUiModel

interface AttachmentPreviewFactory {

    fun type(productPreviewViewModel: InvoicePreviewUiModel): Int

    fun type(sendableVoucherPreview: SendableVoucherPreview): Int

    fun type(product: TopchatProductAttachmentPreviewUiModel): Int

    fun create(
            viewType: Int,
            view: View,
            itemListener: AttachmentPreviewViewHolder.AttachmentItemPreviewListener
    ): AttachmentPreviewViewHolder<*>

}