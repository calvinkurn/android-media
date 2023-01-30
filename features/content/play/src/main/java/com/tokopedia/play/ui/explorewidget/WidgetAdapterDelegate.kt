package com.tokopedia.play.ui.explorewidget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.databinding.ViewPlayGridBinding
import com.tokopedia.play.view.uimodel.ExploreWidgetPlaceholder
import com.tokopedia.play.view.uimodel.WidgetItemUiModel
import com.tokopedia.play.view.uimodel.WidgetUiModel
import com.tokopedia.play.widget.ui.adapter.viewholder.placeholder.PlayWidgetCardPlaceholderViewHolder
import com.tokopedia.play.R as playR

/**
 * @author by astidhiyaa on 02/12/22
 */
class WidgetAdapterDelegate private constructor() {
    internal class Widget(
        private val coordinator: PlayExploreWidgetCoordinator
    ) : TypedAdapterDelegate<WidgetItemUiModel, WidgetUiModel, PlayExploreWidgetViewHolder.Widget>(playR.layout.view_play_grid) {
        override fun onBindViewHolder(
            item: WidgetItemUiModel,
            holder: PlayExploreWidgetViewHolder.Widget
        ) {
            holder.bind(item.item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayExploreWidgetViewHolder.Widget {
            val binding =
                ViewPlayGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PlayExploreWidgetViewHolder.Widget.create(binding, coordinator)
        }
    }

    internal class Shimmering :
        TypedAdapterDelegate<ExploreWidgetPlaceholder, WidgetUiModel, PlayWidgetCardPlaceholderViewHolder>(
            PlayWidgetCardPlaceholderViewHolder.layout
        ) {
        override fun onBindViewHolder(
            item: ExploreWidgetPlaceholder,
            holder: PlayWidgetCardPlaceholderViewHolder
        ) {
            holder.bind()
            holder.setType(PlayWidgetCardPlaceholderViewHolder.Type.MEDIUM)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayWidgetCardPlaceholderViewHolder {
            return PlayWidgetCardPlaceholderViewHolder(basicView)
        }
    }
}
