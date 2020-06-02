package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.model.ProductUiModel
import com.tokopedia.play.broadcaster.ui.viewholder.ProductSelectableViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.ProductSelectableAdapterDelegate

/**
 * Created by jegul on 27/05/20
 */
class ProductSelectableAdapter(
        listener: ProductSelectableViewHolder.Listener
) : BaseDiffUtilAdapter<ProductUiModel>() {

    init {
        delegatesManager
                .addDelegate(ProductSelectableAdapterDelegate(listener))
    }

    override fun areItemsTheSame(oldItem: ProductUiModel, newItem: ProductUiModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ProductUiModel, newItem: ProductUiModel): Boolean {
        return oldItem == newItem
    }
}