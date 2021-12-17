package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.ProductAttachmentUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.*
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.TopchatProductAttachmentListener
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer

open class TopchatProductAttachmentViewHolder constructor(
        itemView: View?,
        private val listener: TopchatProductAttachmentListener,
        private val deferredAttachment: DeferredViewHolderAttachment,
        private val searchListener: SearchListener,
        private val commonListener: CommonViewHolderListener,
        private val adapterListener: AdapterListener
) : BaseChatViewHolder<ProductAttachmentUiModel>(itemView) {

    private var parentMetaData: SingleProductAttachmentContainer.ParentViewHolderMetaData? = null
    private var useStrokeSender = true
    private var productView: SingleProductAttachmentContainer? = itemView?.findViewById(
            R.id.containerProductAttachment
    )

    override fun bind(element: ProductAttachmentUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads.first()) {
            Payload.REBIND -> bind(element)
            DeferredAttachment.PAYLOAD_DEFERRED -> bind(element)
            is SingleProductAttachmentContainer.PayloadUpdateStock -> bindStock(element)
        }
    }

    override fun bind(product: ProductAttachmentUiModel) {
        super.bind(product)
        productView?.bindData(
                product, adapterPosition, listener, deferredAttachment,
                searchListener, commonListener, adapterListener, useStrokeSender,
                parentMetaData
        )
    }

    private fun bindStock(element: ProductAttachmentUiModel) {
        productView?.updateStockState(element)
    }

    fun bind(
        element: ProductAttachmentUiModel, isUnifyBroadcast: Boolean,
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