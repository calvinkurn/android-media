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
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.DeferredViewHolderAttachment
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.SearchListener
import com.tokopedia.topchat.chatroom.view.uimodel.ProductCarouselUiModel
import kotlinx.android.synthetic.main.item_topchat_product_list_attachment.view.*

class ProductCarouselListAttachmentViewHolder constructor(
        itemView: View?,
        productListener: ProductAttachmentListener,
        private val listener: Listener,
        private val deferredAttachment: DeferredViewHolderAttachment,
        private val searchListener: SearchListener
) : BaseChatViewHolder<ProductCarouselUiModel>(itemView) {

    interface Listener {
        fun saveProductCarouselState(position: Int, state: Parcelable?)
        fun getSavedCarouselState(position: Int): Parcelable?
    }

    private val adapter = ProductListAdapter(searchListener, productListener, deferredAttachment)

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
        when (payloads[0]) {
            DeferredAttachment.PAYLOAD_DEFERRED -> bindDeferredAttachment(carousel)
        }
    }

    private fun bindDeferredAttachment(carousel: ProductCarouselUiModel) {
        val attachments = deferredAttachment.getLoadedChatAttachments()
        for (product in carousel.products) {
            if (product is ProductAttachmentViewModel) {
                val attachment = attachments[product.id] ?: continue
                if (attachment is ErrorAttachment) {
                    product.syncError()
                } else {
                    product.updateData(attachment.parsedAttributes)
                }
            }
        }
        bind(carousel)
    }

    override fun bind(carousel: ProductCarouselUiModel) {
        super.bind(carousel)
        bindProductCarousel(carousel)
        bindScrollState()
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