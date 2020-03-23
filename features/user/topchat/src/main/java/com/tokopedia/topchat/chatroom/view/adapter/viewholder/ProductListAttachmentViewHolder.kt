package com.tokopedia.topchat.chatroom.view.adapter.viewholder

import android.os.Parcelable
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.tokopedia.chat_common.view.adapter.viewholder.BaseChatViewHolder
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ProductAttachmentListener
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chatroom.view.adapter.ProductListAdapter
import com.tokopedia.topchat.chatroom.view.custom.ProductListLayoutManager
import com.tokopedia.topchat.chatroom.view.uimodel.ProductCarouselUiModel
import kotlinx.android.synthetic.main.item_topchat_product_list_attachment.view.*

class ProductListAttachmentViewHolder(
        itemView: View?,
        productListener: ProductAttachmentListener,
        private val listener: Listener
) : BaseChatViewHolder<ProductCarouselUiModel>(itemView) {

    interface Listener {
        fun saveProductCarouselState(position: Int, state: Parcelable?)
        fun getSavedCarouselState(position: Int): Parcelable?
    }

    private val adapter = ProductListAdapter(productListener)
    private val layoutManager = ProductListLayoutManager(
            itemView?.context,
            LinearLayoutManager.HORIZONTAL,
            false
    )

    init {
        initRecyclerView()
    }

    private fun initRecyclerView() {
        itemView.rv_product?.apply {
            setHasFixedSize(true)
            layoutManager = this@ProductListAttachmentViewHolder.layoutManager
            adapter = this@ProductListAttachmentViewHolder.adapter
            PagerSnapHelper().attachToRecyclerView(this)
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
        listener.getSavedCarouselState(adapterPosition)?.let {
            layoutManager.onRestoreInstanceState(it)
        }
    }

    override fun onViewRecycled() {
        listener.saveProductCarouselState(adapterPosition, layoutManager.onSaveInstanceState())
        super.onViewRecycled()
    }

    companion object {
        val LAYOUT = R.layout.item_topchat_product_list_attachment
    }
}