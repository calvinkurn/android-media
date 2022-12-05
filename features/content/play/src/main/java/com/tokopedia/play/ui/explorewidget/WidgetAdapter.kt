package com.tokopedia.play.ui.explorewidget

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.view.uimodel.WidgetItemUiModel
import com.tokopedia.play.view.uimodel.WidgetUiModel
import com.tokopedia.play.widget.sample.coordinator.PlayWidgetCoordinator

/**
 * @author by astidhiyaa on 02/12/22
 */
class WidgetAdapter(widgetCoordinator: PlayWidgetCoordinator) :
    BaseDiffUtilAdapter<WidgetUiModel>() {

    init {
        delegatesManager.addDelegate(WidgetAdapterDelegate.Widget(widgetCoordinator))
        delegatesManager.addDelegate(WidgetAdapterDelegate.Chips())
        delegatesManager.addDelegate(WidgetAdapterDelegate.NextPage())
        delegatesManager.addDelegate(WidgetAdapterDelegate.SubSlot())
    }

    override fun areContentsTheSame(oldItem: WidgetUiModel, newItem: WidgetUiModel): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: WidgetUiModel, newItem: WidgetUiModel): Boolean {
        return oldItem == newItem
    }
}
