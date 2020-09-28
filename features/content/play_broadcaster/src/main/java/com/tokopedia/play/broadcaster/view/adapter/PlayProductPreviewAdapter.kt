package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.ui.model.ProductUiModel
import com.tokopedia.play.broadcaster.view.adapter.delegate.PlayProductPreviewAdapterDelegate
import com.tokopedia.play.broadcaster.view.adapter.delegate.PlayProductPreviewLoadingAdapterDelegate

/**
 * Created by jegul on 26/05/20
 */
class PlayProductPreviewAdapter : BaseDiffUtilAdapter<ProductUiModel>() {

    init {
        delegatesManager
                .addDelegate(PlayProductPreviewAdapterDelegate())
                .addDelegate(PlayProductPreviewLoadingAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: ProductUiModel, newItem: ProductUiModel): Boolean {
        return if (oldItem is ProductContentUiModel && newItem is ProductContentUiModel) oldItem.id == newItem.id
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: ProductUiModel, newItem: ProductUiModel): Boolean {
        return oldItem == newItem
    }
}