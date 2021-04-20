package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.data.ProductAttachmentViewModel
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.view.adapter.ProductListAdapter
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.ProductCarouselListAttachmentViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.custom.ProductCarouselRecyclerView
import com.tokopedia.topchat.chatroom.view.uimodel.ProductCarouselUiModel

object ProductCarouselListAttachmentViewHolderBinder {

    fun initRecyclerView(
            recyclerView: ProductCarouselRecyclerView?,
            adapterListener: AdapterListener,
            rvAdapter: ProductListAdapter,
            listener: ProductCarouselListAttachmentViewHolder.Listener,
            viewHolder: RecyclerView.ViewHolder
    ) {
        recyclerView?.apply {
            setHasFixedSize(true)
            setRecycledViewPool(adapterListener.getProductCarouselViewPool())
            adapter = rvAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        saveProductCarouselState(viewHolder.adapterPosition, listener)
                    }
                }
            })
        }
    }

    fun bindDeferredAttachment(
            carousel: ProductCarouselUiModel,
            deferredAttachment: DeferredViewHolderAttachment
    ) {
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

    fun bindProductCarousel(
            carousel: ProductCarouselUiModel,
            adapter: ProductListAdapter
    ) {
        adapter.carousel = carousel
    }

    fun bindScrollState(
            rv: ProductCarouselRecyclerView?,
            listener: ProductCarouselListAttachmentViewHolder.Listener,
            vh: RecyclerView.ViewHolder
    ) {
        rv?.restoreSavedCarouselState(vh.adapterPosition, listener)
    }
}