package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.topchat.chatroom.domain.pojo.chatattachment.ErrorAttachment
import com.tokopedia.topchat.chatroom.view.adapter.MultipleProductBundlingAdapter
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling.ProductBundlingCarouselViewHolder
import com.tokopedia.topchat.chatroom.view.custom.product_bundling.BundleSpaceItemDecoration
import com.tokopedia.topchat.chatroom.view.custom.product_bundling.ProductBundlingCardAttachmentContainer
import com.tokopedia.topchat.chatroom.view.custom.product_bundling.ProductBundlingRecyclerView
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.MultipleProductBundlingUiModel

object ProductBundlingViewHolderBinder {

    private const val SPACE_DECORATION = 3

    fun initRecyclerView(
        recyclerView: ProductBundlingRecyclerView?,
        adapterListener: AdapterListener,
        recyclerViewAdapter: MultipleProductBundlingAdapter,
        productBundlingCarouselListener: ProductBundlingCarouselViewHolder.Listener,
        viewHolder: RecyclerView.ViewHolder,
        source: ProductBundlingCardAttachmentContainer.BundlingSource
    ) {
        recyclerView?.apply {
            setRecycledViewPool(adapterListener.getCarouselViewPool())
            adapter = recyclerViewAdapter
            addBundleItemDecoration(this, source)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        saveProductCarouselState(viewHolder.adapterPosition, productBundlingCarouselListener)
                    }
                }
            })
        }
    }

    private fun addBundleItemDecoration(
        recyclerView: ProductBundlingRecyclerView?,
        source: ProductBundlingCardAttachmentContainer.BundlingSource
    ) {
        val counter = recyclerView?.itemDecorationCount ?: 0
        for (i in 0 until counter) {
            recyclerView?.removeItemDecorationAt(i)
        }
        if (isProductBundlingBroadcast(source)) {
            recyclerView?.addItemDecoration(BundleSpaceItemDecoration(SPACE_DECORATION))
        }
    }

    private fun isProductBundlingBroadcast(
        source: ProductBundlingCardAttachmentContainer.BundlingSource
    ): Boolean {
        return source ==
                ProductBundlingCardAttachmentContainer
                    .BundlingSource
                    .BROADCAST_ATTACHMENT_MULTIPLE
    }

    fun bindDeferredAttachment(
        element: MultipleProductBundlingUiModel,
        deferredAttachment: DeferredViewHolderAttachment
    ) {
        if (!element.isLoading) return
        val chatAttachments = deferredAttachment.getLoadedChatAttachments()
        val attachment = chatAttachments[element.attachmentId] ?: return
        if (attachment is ErrorAttachment) {
            element.syncError()
        } else {
            element.updateData(attachment.parsedAttributes)
        }
    }

    fun bindProductBundling(
        recyclerViewAdapter: MultipleProductBundlingAdapter,
        carouselBundling: MultipleProductBundlingUiModel,
        source: ProductBundlingCardAttachmentContainer.BundlingSource?= null
    ) {
        recyclerViewAdapter.source = source
        recyclerViewAdapter.carousel = carouselBundling
    }

    fun bindScrollState(
        recyclerView: ProductBundlingRecyclerView?,
        productBundlingCarouselListener: ProductBundlingCarouselViewHolder.Listener,
        viewHolder: RecyclerView.ViewHolder
    ) {
        recyclerView?.restoreSavedCarouselState(
            viewHolder.adapterPosition, productBundlingCarouselListener)
    }
}