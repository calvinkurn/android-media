package com.tokopedia.play.ui.explorewidget

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
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
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: WidgetUiModel, newItem: WidgetUiModel): Boolean =
        oldItem == newItem
}
