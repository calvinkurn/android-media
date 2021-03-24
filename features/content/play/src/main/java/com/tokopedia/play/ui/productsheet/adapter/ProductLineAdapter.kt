package com.tokopedia.play.ui.productsheet.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.productsheet.adapter.delegate.ProductLineAdapterDelegate
import com.tokopedia.play.ui.productsheet.adapter.delegate.ProductPlaceholderAdapterDelegate
import com.tokopedia.play.ui.productsheet.viewholder.ProductLineViewHolder
import com.tokopedia.play.view.uimodel.PlayProductUiModel

/**
 * Created by jegul on 03/03/20
 */
class ProductLineAdapter(
        listener: ProductLineViewHolder.Listener
) : BaseDiffUtilAdapter<PlayProductUiModel>(), ProductLineViewHolder.Listener by listener {

    init {
        delegatesManager
                .addDelegate(ProductLineAdapterDelegate(this))
                .addDelegate(ProductPlaceholderAdapterDelegate())
    }

    override fun areItemsTheSame(oldItem: PlayProductUiModel, newItem: PlayProductUiModel): Boolean {
        return if (oldItem is PlayProductUiModel.Product && newItem is PlayProductUiModel.Product) oldItem.id == newItem.id
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PlayProductUiModel, newItem: PlayProductUiModel): Boolean {
        return oldItem == newItem
    }
}