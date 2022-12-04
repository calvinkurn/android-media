package com.tokopedia.play.ui.explorewidget

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.view.uimodel.WidgetUiModel
import com.tokopedia.play.widget.sample.coordinator.PlayWidgetCoordinator

/**
 * @author by astidhiyaa on 02/12/22
 */
class WidgetAdapter(private val widgetCoordinator: PlayWidgetCoordinator) :
    BaseDiffUtilAdapter<WidgetUiModel.WidgetItemUiModel>() {

    init {
        delegatesManager.addDelegate(WidgetAdapterDelegate.Widget(widgetCoordinator))
    }

    override fun areItemsTheSame(
        oldItem: WidgetUiModel.WidgetItemUiModel,
        newItem: WidgetUiModel.WidgetItemUiModel
    ): Boolean {
        //TODO () change per id
        return oldItem.item == newItem.item
    }

    override fun areContentsTheSame(
        oldItem: WidgetUiModel.WidgetItemUiModel,
        newItem: WidgetUiModel.WidgetItemUiModel
    ): Boolean {
        return oldItem.item == newItem.item
    }
}
