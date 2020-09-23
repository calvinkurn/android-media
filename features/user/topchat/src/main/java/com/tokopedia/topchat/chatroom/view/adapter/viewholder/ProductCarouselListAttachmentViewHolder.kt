package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.data.DeferredAttachment
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.view.adapter.ProductListAdapter
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.CommonViewHolderListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.uimodel.ProductCarouselUiModel
import kotlinx.android.synthetic.main.item_topchat_product_list_attachment.view.*

class ProductCarouselListAttachmentViewHolder constructor(
        itemView: View?,
        productListener: ProductAttachmentListener,
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

    private val adapter = ProductListAdapter(
            searchListener, productListener, deferredAttachment, commonListener, adapterListener
    )

    init {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        itemView.rv_product?.apply {
            setHasFixedSize(true)
            adapter = this@ProductCarouselListAttachmentViewHolder.adapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        saveProductCarouselState(adapterPosition, listener)
                    }
                }
            })
        }
    }

    override fun bind(carousel: ProductCarouselUiModel, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return
        when (val payload = payloads[0]) {
            DeferredAttachment.PAYLOAD_DEFERRED -> bind(carousel)
            is TopchatProductAttachmentViewHolder.OccState -> bindNewOccState(payload)
        }
    }

    override fun bind(carousel: ProductCarouselUiModel) {
        super.bind(carousel)
        bindDeferredAttachment(carousel)
        bindProductCarousel(carousel)
        bindScrollState()
    }

    private fun bindNewOccState(payload: TopchatProductAttachmentViewHolder.OccState) {
        adapter.notifyItemChanged(payload.childPosition, payload)
    }

    private fun bindDeferredAttachment(carousel: ProductCarouselUiModel) {
        if (!carousel.isLoading()) return
        val attachments = deferredAttachment.getLoadedChatAttachments()
        for (product in carousel.products) {
            if (product is ProductAttachmentViewModel) {
                val attachment = attachments[product.id] ?: return
                if (attachment is ErrorAttachment) {
                    product.syncError()
                } else {
                    product.updateData(attachment.parsedAttributes)
                }
            }
        }
        if (carousel.isBroadCast()) {
            carousel.products = carousel.products.sortedBy {
                return@sortedBy (it as ProductAttachmentViewModel).hasEmptyStock()
            }
        }
    }

    private fun bindProductCarousel(carousel: ProductCarouselUiModel) {
        adapter.carousel = carousel
    }

    private fun bindScrollState() {
        itemView.rv_product?.restoreSavedCarouselState(adapterPosition, listener)
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_product_list_attachment
    }
}