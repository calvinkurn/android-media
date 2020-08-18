package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.view.adapter.delegate.PlayProductLiveAdapterDelegate

/**
 * Created by jegul on 11/06/20
 */
class PlayProductLiveAdapter : BaseDiffUtilAdapter<ProductContentUiModel>() {

    init {
        delegatesManager
                .addDelegate(PlayProductLiveAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: ProductContentUiModel, newItem: ProductContentUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductContentUiModel, newItem: ProductContentUiModel): Boolean {
        return oldItem == newItem
    }
}