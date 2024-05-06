package com.tokopedia.play.ui.explorewidget.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.explorewidget.PlayExploreWidgetCoordinator
import com.tokopedia.play.ui.explorewidget.adapter.delegate.WidgetAdapterDelegate
import com.tokopedia.play.view.uimodel.TabMenuUiModel
import com.tokopedia.play.view.uimodel.ExploreWidgetItemUiModel
import com.tokopedia.play.view.uimodel.WidgetUiModel

/**
 * @author by astidhiyaa on 02/12/22
 */
class WidgetAdapter(coordinator: PlayExploreWidgetCoordinator) :
    BaseDiffUtilAdapter<WidgetUiModel>() { //todo() remove coordinator, etc, adjust analytics
    init {
        delegatesManager.addDelegate(WidgetAdapterDelegate.Widget(coordinator))
        delegatesManager.addDelegate(WidgetAdapterDelegate.Shimmering())
    }

    override fun areItemsTheSame(oldItem: WidgetUiModel, newItem: WidgetUiModel): Boolean {
        return if (oldItem is ExploreWidgetItemUiModel && newItem is ExploreWidgetItemUiModel) oldItem.id == newItem.id
        else if (oldItem is TabMenuUiModel && newItem is TabMenuUiModel) oldItem.items == newItem.items
        else oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: WidgetUiModel, newItem: WidgetUiModel): Boolean =
        oldItem == newItem
}
