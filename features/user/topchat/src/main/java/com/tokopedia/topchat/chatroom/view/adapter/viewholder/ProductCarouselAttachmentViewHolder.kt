package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment

class ProductCarouselAttachmentViewHolder(
        itemView: View?,
        listener: ProductAttachmentListener,
        deferredAttachment: DeferredViewHolderAttachment
) : TopchatProductAttachmentViewHolder(itemView, listener, deferredAttachment) {

    companion object {
        val LAYOUT = R.layout.item_topchat_product_carousel_attachment
    }
}