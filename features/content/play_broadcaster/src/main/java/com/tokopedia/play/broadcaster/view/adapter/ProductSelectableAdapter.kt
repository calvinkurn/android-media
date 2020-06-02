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
) : BaseDiffUtilAdapter<Any>() {

    init {
        delegatesManager
                .addDelegate(ProductSelectableAdapterDelegate(listener))
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is ProductUiModel && newItem is ProductUiModel) oldItem.id == newItem.id
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem == newItem
    }
}