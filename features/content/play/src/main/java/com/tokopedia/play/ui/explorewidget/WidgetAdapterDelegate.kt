package com.tokopedia.play.ui.explorewidget

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.view.uimodel.WidgetItemUiModel
import com.tokopedia.play.view.uimodel.WidgetUiModel
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.coordinator.PlayWidgetCoordinator

/**
 * @author by astidhiyaa on 02/12/22
 */
class WidgetAdapterDelegate {
    internal class Widget(private val widgetCoordinator: PlayWidgetCoordinator) :
        TypedAdapterDelegate<WidgetItemUiModel, WidgetUiModel, PlayWidgetViewHolder>(PlayWidgetViewHolder.layout) {
        override fun onBindViewHolder(
            item: WidgetItemUiModel,
            holder: PlayWidgetViewHolder
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayWidgetViewHolder {
            return PlayWidgetViewHolder(basicView, widgetCoordinator)
        }
    }
}
