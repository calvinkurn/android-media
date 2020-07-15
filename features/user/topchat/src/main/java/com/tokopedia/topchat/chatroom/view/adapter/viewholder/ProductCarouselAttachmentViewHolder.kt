package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.topchat.R

class ProductCarouselAttachmentViewHolder(
        itemView: View?,
        listener: ProductAttachmentListener
) : TopchatProductAttachmentViewHolder(itemView, listener) {

    companion object {
        val LAYOUT = R.layout.item_topchat_product_carousel_attachment
    }
}