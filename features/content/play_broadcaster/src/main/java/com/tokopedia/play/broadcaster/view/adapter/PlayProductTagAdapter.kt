package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductUiModel
import com.tokopedia.play.broadcaster.view.adapter.delegate.ProductTagAdapterDelegate
import com.tokopedia.play.broadcaster.view.adapter.delegate.ProductTagPlaceholderAdapterDelegate

/**
 * Created By : Jonathan Darwin on November 24, 2021
 */
class PlayProductTagAdapter: BaseDiffUtilAdapter<ProductUiModel>() {

    init {
        delegatesManager
            .addDelegate(ProductTagAdapterDelegate())
            .addDelegate(ProductTagPlaceholderAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: ProductUiModel, newItem: ProductUiModel): Boolean {
        return if(oldItem is ProductContentUiModel && newItem is ProductContentUiModel) oldItem.id == newItem.id
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ProductUiModel, newItem: ProductUiModel): Boolean {
        return oldItem == newItem
    }
}