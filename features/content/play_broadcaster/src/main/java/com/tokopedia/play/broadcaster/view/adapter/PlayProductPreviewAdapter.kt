package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.ProductUiModel
import com.tokopedia.play.broadcaster.view.adapter.delegate.PlayProductPreviewAdapterDelegate

/**
 * Created by jegul on 26/05/20
 */
class PlayProductPreviewAdapter : BaseDiffUtilAdapter<ProductUiModel>() {

    init {
        delegatesManager
                .addDelegate(PlayProductPreviewAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: ProductUiModel, newItem: ProductUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductUiModel, newItem: ProductUiModel): Boolean {
        return oldItem == newItem
    }
}