package com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling

import android.view.View
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.ProductBundlingListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.*
import com.tokopedia.topchat.chatroom.view.custom.product_bundling.ProductBundlingCardAttachmentContainer
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.ProductBundlingUiModel

class ProductBundlingCardViewHolder(
    itemView: View,
    private val listener: ProductBundlingListener,
    private val adapterListener: AdapterListener,
    private val searchListener: SearchListener,
    private val commonListener: CommonViewHolderListener,
    private val deferredAttachment: DeferredViewHolderAttachment,
    private val source: ProductBundlingCardAttachmentContainer.BundlingSource? =
        ProductBundlingCardAttachmentContainer.BundlingSource.PRODUCT_ATTACHMENT
) : BaseChatViewHolder<ProductBundlingUiModel>(itemView) {

    private val containerBundlingAttachment:
            ProductBundlingCardAttachmentContainer? =
        itemView.findViewById(R.id.containerBundlingAttachment)

    override fun bind(element: ProductBundlingUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads.first()) {
            Payload.REBIND -> bind(element)
            DeferredAttachment.PAYLOAD_DEFERRED -> bind(element)
        }
    }

    override fun bind(uiModel: ProductBundlingUiModel) {
        containerBundlingAttachment?.bindData(
            uiModel, adapterPosition, listener, adapterListener,
            searchListener, commonListener, deferredAttachment,
            source
        )
    }

    companion object {
        val LAYOUT_CAROUSEL = R.layout.item_topchat_product_bundling_carousel
        val LAYOUT_SINGLE = R.layout.item_topchat_product_bundling
    }
}