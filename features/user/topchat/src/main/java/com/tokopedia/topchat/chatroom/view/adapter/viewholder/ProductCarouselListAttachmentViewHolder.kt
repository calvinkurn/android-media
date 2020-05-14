package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.ProductListAdapter
import com.tokopedia.topchat.chatroom.view.uimodel.ProductCarouselUiModel
import kotlinx.android.synthetic.main.item_topchat_product_list_attachment.view.*

class ProductCarouselListAttachmentViewHolder(
        itemView: View?,
        productListener: ProductAttachmentListener,
        private val listener: Listener
) : BaseChatViewHolder<ProductCarouselUiModel>(itemView) {

    interface Listener {
        fun saveProductCarouselState(position: Int, state: Parcelable?)
        fun getSavedCarouselState(position: Int): Parcelable?
    }

    private val adapter = ProductListAdapter(productListener)

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

    override fun bind(carousel: ProductCarouselUiModel?) {
        if (carousel == null) return
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