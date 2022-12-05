package com.tokopedia.play.ui.explorewidget

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.view.uimodel.WidgetItemUiModel
import com.tokopedia.play.widget.sample.coordinator.PlayWidgetCoordinator

/**
 * @author by astidhiyaa on 02/12/22
 */
class WidgetAdapter(private val widgetCoordinator: PlayWidgetCoordinator) :
    BaseDiffUtilAdapter<WidgetItemUiModel>() {

    init {
        delegatesManager.addDelegate(WidgetAdapterDelegate.Widget(widgetCoordinator))
    }

    override fun areItemsTheSame(
        oldItem: WidgetItemUiModel,
        newItem: WidgetItemUiModel
    ): Boolean {
        //TODO () change per id
        return oldItem.item == newItem.item
    }

    override fun areContentsTheSame(
        oldItem: WidgetItemUiModel,
        newItem: WidgetItemUiModel
    ): Boolean {
        return oldItem.item == newItem.item
    }
}
