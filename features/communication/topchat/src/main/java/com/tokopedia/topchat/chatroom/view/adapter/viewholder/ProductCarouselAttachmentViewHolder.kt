package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.TopchatProductAttachmentListener

class ProductCarouselAttachmentViewHolder constructor(
        itemView: View?,
        listener: TopchatProductAttachmentListener,
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