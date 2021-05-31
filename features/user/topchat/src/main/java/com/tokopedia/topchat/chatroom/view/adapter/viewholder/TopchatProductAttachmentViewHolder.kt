package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.TopchatProductAttachmentListener
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer

open class TopchatProductAttachmentViewHolder constructor(
        itemView: View?,
        private val listener: TopchatProductAttachmentListener,
        private val deferredAttachment: DeferredViewHolderAttachment,
        private val searchListener: SearchListener,
        private val commonListener: CommonViewHolderListener,
        private val adapterListener: AdapterListener
) : BaseChatViewHolder<ProductAttachmentViewModel>(itemView) {

    private var parentMetaData: SingleProductAttachmentContainer.ParentViewHolderMetaData? = null
    private var useStrokeSender = true
    private var productView: SingleProductAttachmentContainer? = itemView?.findViewById(
            R.id.containerProductAttachment
    )

    override fun bind(element: ProductAttachmentViewModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads.first()) {
            DeferredAttachment.PAYLOAD_DEFERRED -> bind(element)
            is SingleProductAttachmentContainer.PayloadUpdateStock -> bindStock(element)
        }
    }

    override fun bind(product: ProductAttachmentViewModel) {
        super.bind(product)
        productView?.bindData(
                product, adapterPosition, listener, deferredAttachment,
                searchListener, commonListener, adapterListener, useStrokeSender,
                parentMetaData
        )
    }

    private fun bindStock(element: ProductAttachmentViewModel) {
        productView?.updateStockState(element)
    }

    fun bind(
            element: ProductAttachmentViewModel, isUnifyBroadcast: Boolean,
            parentMetaData: SingleProductAttachmentContainer.ParentViewHolderMetaData?
    ) {
        this.useStrokeSender = !isUnifyBroadcast
        this.parentMetaData = parentMetaData
        bind(element)
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_product_attachment
    }

}