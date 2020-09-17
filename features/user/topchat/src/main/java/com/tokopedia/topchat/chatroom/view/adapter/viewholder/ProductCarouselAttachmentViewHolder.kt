package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener

class ProductCarouselAttachmentViewHolder constructor(
        itemView: View?,
        listener: ProductAttachmentListener,
        deferredAttachment: DeferredViewHolderAttachment,
        searchListener: SearchListener,
        commonListener: CommonViewHolderListener,
        adapterListener: AdapterListener
) : TopchatProductAttachmentViewHolder(
        itemView, listener, deferredAttachment, searchListener, commonListener, adapterListener
) {

    companion object {
        val LAYOUT = R.layout.item_topchat_product_carousel_attachment
    }
}