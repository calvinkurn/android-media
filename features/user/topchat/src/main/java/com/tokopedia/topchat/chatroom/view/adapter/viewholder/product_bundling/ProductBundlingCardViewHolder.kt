package com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.custom.product_bundling.ProductBundlingCardAttachmentContainer
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.ProductBundlingUiModel

class ProductBundlingCardViewHolder(
    itemView: View,
    private val listener: ProductBundlingListener,
    private val adapterListener: AdapterListener,
    private val deferredAttachment: DeferredViewHolderAttachment,
    private val searchListener: SearchListener,
    private val commonListener: CommonViewHolderListener,
) : RecyclerView.ViewHolder(itemView) {

    private val containerBundlingAttachment:
            ProductBundlingCardAttachmentContainer? =
        itemView.findViewById(R.id.containerBundlingAttachment)

    fun bind(element: ProductBundlingUiModel) {
        containerBundlingAttachment?.bindData(
            element, adapterPosition, listener, adapterListener,
            deferredAttachment, searchListener, commonListener
        )
    }

    companion object {
        val LAYOUT_CAROUSEL = R.layout.item_topchat_product_bundling_carousel
        val LAYOUT_SINGLE = R.layout.item_topchat_product_bundling
    }
}