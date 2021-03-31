package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.ProductListAdapter
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder.ProductCarouselListAttachmentViewHolderBinder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.listener.TopchatProductAttachmentListener
import com.tokopedia.topchat.chatroom.view.custom.ProductCarouselRecyclerView
import com.tokopedia.topchat.chatroom.view.custom.SingleProductAttachmentContainer
import com.tokopedia.topchat.chatroom.view.uimodel.ProductCarouselUiModel

class ProductCarouselListAttachmentViewHolder constructor(
        itemView: View?,
        productListener: TopchatProductAttachmentListener,
        private val listener: Listener,
        private val deferredAttachment: DeferredViewHolderAttachment,
        private val searchListener: SearchListener,
        private val commonListener: CommonViewHolderListener,
        private val adapterListener: AdapterListener
) : BaseChatViewHolder<ProductCarouselUiModel>(itemView) {

    interface Listener {
        fun saveProductCarouselState(position: Int, state: Parcelable?)
        fun getSavedCarouselState(position: Int): Parcelable?
    }

    private val rv: ProductCarouselRecyclerView? = itemView?.findViewById(R.id.rv_product)
    private val adapter = ProductListAdapter(
            searchListener, productListener, deferredAttachment, commonListener, adapterListener
    )

    init {
        ProductCarouselListAttachmentViewHolderBinder.initRecyclerView(
                rv, adapterListener, adapter, listener, this
        )
    }

    override fun bind(carousel: ProductCarouselUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (val payload = payloads[0]) {
            DeferredAttachment.PAYLOAD_DEFERRED -> {
                bind(carousel)
            }
            is SingleProductAttachmentContainer.PayloadUpdateStock -> {
                ProductCarouselListAttachmentViewHolderBinder.updateCarouselProductStock(
                        adapter, payload
                )
            }
        }
    }

    override fun bind(carousel: ProductCarouselUiModel) {
        super.bind(carousel)
        ProductCarouselListAttachmentViewHolderBinder.updateParentMetaData(
                carousel, adapterPosition, adapter
        )
        ProductCarouselListAttachmentViewHolderBinder.bindDeferredAttachment(
                carousel, deferredAttachment
        )
        ProductCarouselListAttachmentViewHolderBinder.bindProductCarousel(carousel, adapter)
        ProductCarouselListAttachmentViewHolderBinder.bindScrollState(rv, listener, this)
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_product_list_attachment
    }
}