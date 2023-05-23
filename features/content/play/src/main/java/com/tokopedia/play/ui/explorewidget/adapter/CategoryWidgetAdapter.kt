package com.tokopedia.play.ui.explorewidget.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.explorewidget.adapter.delegate.CategoryWidgetAdapterDelegate
import com.tokopedia.play.ui.explorewidget.viewholder.CategoryWidgetViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel

/**
 * @author by astidhiyaa on 23/05/23
 */
internal class CategoryWidgetAdapter(listener: CategoryWidgetViewHolder.Item.Listener) :
    BaseDiffUtilAdapter<PlayWidgetItemUiModel>() {

    init {
        delegatesManager.addDelegate(CategoryWidgetAdapterDelegate.Card(listener))
        delegatesManager.addDelegate(CategoryWidgetAdapterDelegate.Shimmer)
    }

    override fun areItemsTheSame(
        oldItem: PlayWidgetItemUiModel,
        newItem: PlayWidgetItemUiModel
    ): Boolean {
        return oldItem == newItem //TODO() Temp
    }

    override fun areContentsTheSame(
        oldItem: PlayWidgetItemUiModel,
        newItem: PlayWidgetItemUiModel
    ): Boolean {
        return oldItem == newItem //TODO() Temp
    }
}
