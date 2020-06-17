package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.PlayCoverProductViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.PlayCoverProductAdapterDelegate

/**
 * @author by furqan on 07/06/2020
 */
class PlayCoverProductAdapter(listener: PlayCoverProductViewHolder.Listener) : BaseDiffUtilAdapter<ProductContentUiModel>() {

    init {
        delegatesManager
                .addDelegate(PlayCoverProductAdapterDelegate(listener))
    }

    override fun areItemsTheSame(oldItem: ProductContentUiModel, newItem: ProductContentUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductContentUiModel, newItem: ProductContentUiModel): Boolean {
        return oldItem == newItem
    }
}