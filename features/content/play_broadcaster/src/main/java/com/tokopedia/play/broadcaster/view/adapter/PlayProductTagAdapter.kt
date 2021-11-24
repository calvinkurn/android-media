package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.view.adapter.delegate.ProductTagAdapterDelegate

/**
 * Created By : Jonathan Darwin on November 24, 2021
 */
class PlayProductTagAdapter: BaseDiffUtilAdapter<ProductContentUiModel>() {

    init {
        delegatesManager
            .addDelegate(ProductTagAdapterDelegate())
    }

    override fun areItemsTheSame(
        oldItem: ProductContentUiModel,
        newItem: ProductContentUiModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ProductContentUiModel,
        newItem: ProductContentUiModel
    ): Boolean {
        return oldItem == newItem
    }
}