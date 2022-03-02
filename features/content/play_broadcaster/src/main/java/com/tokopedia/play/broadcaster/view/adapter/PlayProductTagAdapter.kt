package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.product.ProductUiModel
import com.tokopedia.play.broadcaster.view.adapter.delegate.ProductCarouselAdapterDelegate

/**
 * Created By : Jonathan Darwin on November 24, 2021
 */
class PlayProductTagAdapter: BaseDiffUtilAdapter<Any>() {

    init {
//        delegatesManager
//            .addDelegate(ProductTagAdapterDelegate())
//            .addDelegate(ProductTagPlaceholderAdapterDelegate())

        delegatesManager
            .addDelegate(ProductCarouselAdapterDelegate.Product())
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