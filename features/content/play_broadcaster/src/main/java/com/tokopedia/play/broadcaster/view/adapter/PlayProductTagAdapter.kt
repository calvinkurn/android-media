package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.carousel.ProductCarouselViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.ProductCarouselAdapterDelegate

/**
 * Created By : Jonathan Darwin on November 24, 2021
 */
internal class PlayProductTagAdapter(private val listener: ProductCarouselViewHolder.Product.Listener, ): BaseDiffUtilAdapter<Any>() {

    init {
        delegatesManager
            .addDelegate(ProductCarouselAdapterDelegate.Product(listener))
            .addDelegate(ProductCarouselAdapterDelegate.Loading())
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if(oldItem is ProductUiModel && newItem is ProductUiModel) oldItem.id == newItem.id
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem == newItem
    }
}