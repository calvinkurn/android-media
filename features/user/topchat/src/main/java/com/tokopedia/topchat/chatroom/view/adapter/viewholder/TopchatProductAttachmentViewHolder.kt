package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.view.View
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.TopchatProductAttachmentViewHolderBinder
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer

open class TopchatProductAttachmentViewHolder constructor(
        itemView: View?,
        private val listener: ProductAttachmentListener,
        private val deferredAttachment: DeferredViewHolderAttachment,
        private val searchListener: SearchListener,
        private val commonListener: CommonViewHolderListener,
        private val adapterListener: AdapterListener
) : BaseChatViewHolder<ProductAttachmentViewModel>(itemView) {

    private var useStrokeSender = true
    private var productView: SingleProductAttachmentContainer? = itemView?.findViewById(R.id.containerProductAttachment)

    override fun bind(element: ProductAttachmentViewModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (payloads[0]) {
            DeferredAttachment.PAYLOAD_DEFERRED -> bind(element)
            is OccState -> TopchatProductAttachmentViewHolderBinder.bindNewOccState(element, productView)
        }
    }

    override fun bind(product: ProductAttachmentViewModel) {
        super.bind(product)
        productView?.bindData(
                product, adapterPosition, listener, deferredAttachment,
                searchListener, commonListener, adapterListener, useStrokeSender
        )
    }

    fun bind(element: ProductAttachmentViewModel, isUnifyBroadcast: Boolean) {
        useStrokeSender = !isUnifyBroadcast
        bind(element)
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_product_attachment

        const val VARIANT_DEFAULT = "No OCC"
        const val VARIANT_A = "ATC OCC"
        const val VARIANT_B = "OCC Only"
        const val AB_TEST_KEY = "OCC at TopChat"
    }

    data class OccState(
            val parentPosition: Int,
            val childPosition: Int = -1 // for carousel
    )
}