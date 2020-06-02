package com.tokopedia.play.broadcaster.view.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.broadcaster.ui.viewholder.PlayEtalaseViewHolder
import com.tokopedia.play.broadcaster.ui.viewholder.ProductSelectableViewHolder
import com.tokopedia.play.broadcaster.view.adapter.delegate.PlayEtalaseAdapterDelegate
import com.tokopedia.play.broadcaster.view.adapter.delegate.ProductSelectableAdapterDelegate
import com.tokopedia.play.broadcaster.view.uimodel.PlayEtalaseUiModel
import com.tokopedia.play.broadcaster.view.uimodel.ProductUiModel

/**
 * Created by jegul on 26/05/20
 */
class PlayEtalaseAdapter(
        etalaseListener: PlayEtalaseViewHolder.Listener,
        selectableListener: ProductSelectableViewHolder.Listener
) : BaseDiffUtilAdapter<Any>() {

    init {
        delegatesManager
                .addDelegate(PlayEtalaseAdapterDelegate(etalaseListener))
                .addDelegate(ProductSelectableAdapterDelegate(selectableListener))
    }

    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return if (oldItem is PlayEtalaseUiModel && newItem is PlayEtalaseUiModel) oldItem.id == newItem.id
        else if (oldItem is ProductUiModel && newItem is ProductUiModel) oldItem.id == newItem.id
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem == newItem
    }
}