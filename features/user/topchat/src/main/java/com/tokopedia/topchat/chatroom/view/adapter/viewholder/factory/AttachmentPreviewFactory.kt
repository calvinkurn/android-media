package com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory

import android.view.View
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment.AttachmentPreviewViewHolder
import com.tokopedia.topchat.chatroom.view.viewmodel.InvoicePreviewUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableProductPreview
import com.tokopedia.topchat.chatroom.view.viewmodel.SendableVoucherPreview

interface AttachmentPreviewFactory {

    fun type(sendableProductPreview: SendableProductPreview): Int

    fun type(productPreviewViewModel: InvoicePreviewUiModel): Int

    fun type(sendableVoucherPreview: SendableVoucherPreview): Int

    fun create(
            viewType: Int,
            view: View,
            itemListener: AttachmentPreviewViewHolder.AttachmentItemPreviewListener
    ): AttachmentPreviewViewHolder<*>

}