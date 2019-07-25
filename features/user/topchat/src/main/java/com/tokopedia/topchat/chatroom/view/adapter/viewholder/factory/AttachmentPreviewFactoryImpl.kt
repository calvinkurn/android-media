package com.tokopedia.topchat.chatroom.view.adapter.viewholder.factory

import android.view.View
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment.AttachmentPreviewViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.previewattachment.ProductPreviewViewHolder
import com.tokopedia.topchat.chatroom.view.viewmodel.ProductPreviewViewModel

class AttachmentPreviewFactoryImpl : AttachmentPreviewFactory {

    override fun type(productPreviewViewModel: ProductPreviewViewModel): Int {
        return ProductPreviewViewHolder.LAYOUT
    }

    override fun create(
            viewType: Int,
            view: View,
            itemListener: AttachmentPreviewViewHolder.AttachmentItemPreviewListener
    ): AttachmentPreviewViewHolder<*> {
        return when (viewType) {
            ProductPreviewViewHolder.LAYOUT -> ProductPreviewViewHolder(view, itemListener)
            else -> throw IllegalStateException("Unknown View Type: $viewType")
        }
    }

}