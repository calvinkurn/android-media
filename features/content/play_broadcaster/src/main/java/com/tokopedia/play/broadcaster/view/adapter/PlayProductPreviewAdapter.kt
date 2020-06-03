package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.view.adapter.delegate.PlayProductPreviewAdapterDelegate

/**
 * Created by jegul on 26/05/20
 */
class PlayProductPreviewAdapter : BaseDiffUtilAdapter<ProductContentUiModel>() {

    init {
        delegatesManager
                .addDelegate(PlayProductPreviewAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: ProductContentUiModel, newItem: ProductContentUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductContentUiModel, newItem: ProductContentUiModel): Boolean {
        return oldItem == newItem
    }
}