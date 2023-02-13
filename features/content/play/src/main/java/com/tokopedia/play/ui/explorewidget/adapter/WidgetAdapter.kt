package com.tokopedia.play.ui.explorewidget.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.explorewidget.PlayExploreWidgetCoordinator
import com.tokopedia.play.ui.explorewidget.adapter.delegate.WidgetAdapterDelegate
import com.tokopedia.play.view.uimodel.TabMenuUiModel
import com.tokopedia.play.view.uimodel.WidgetItemUiModel
import com.tokopedia.play.view.uimodel.WidgetUiModel

/**
 * @author by astidhiyaa on 02/12/22
 */
class WidgetAdapter(coordinator: PlayExploreWidgetCoordinator) :
    BaseDiffUtilAdapter<WidgetUiModel>() {
    init {
        delegatesManager.addDelegate(WidgetAdapterDelegate.Widget(coordinator))
        delegatesManager.addDelegate(WidgetAdapterDelegate.Shimmering())
    }

    override fun areItemsTheSame(oldItem: WidgetUiModel, newItem: WidgetUiModel): Boolean {
        return if (oldItem is WidgetItemUiModel && newItem is WidgetItemUiModel) oldItem.item == newItem.item
        else if (oldItem is TabMenuUiModel && newItem is TabMenuUiModel) oldItem.items == newItem.items
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: WidgetUiModel, newItem: WidgetUiModel): Boolean =
        oldItem == newItem
}
