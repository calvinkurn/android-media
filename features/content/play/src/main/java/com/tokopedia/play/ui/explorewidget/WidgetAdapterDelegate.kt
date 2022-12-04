package com.tokopedia.play.ui.explorewidget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.databinding.ViewWidgetHolderBinding
import com.tokopedia.play.view.uimodel.WidgetUiModel
import com.tokopedia.play.widget.sample.coordinator.PlayWidgetCoordinator

/**
 * @author by astidhiyaa on 02/12/22
 */
class WidgetAdapterDelegate {
    internal class Widget(private val widgetCoordinator: PlayWidgetCoordinator): TypedAdapterDelegate<WidgetUiModel.WidgetItemUiModel, WidgetUiModel.WidgetItemUiModel, WidgetItemViewHolder.Medium>(R.layout.view_widget_holder){
        override fun onBindViewHolder(
            item: WidgetUiModel.WidgetItemUiModel,
            holder: WidgetItemViewHolder.Medium
        ) {
            holder.bind(item.item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): WidgetItemViewHolder.Medium {
            val view = ViewWidgetHolderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
            return WidgetItemViewHolder.Medium(widgetCoordinator, view)
        }
    }
}
