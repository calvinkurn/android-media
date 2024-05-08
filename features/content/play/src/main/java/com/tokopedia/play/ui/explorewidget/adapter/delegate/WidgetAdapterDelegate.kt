package com.tokopedia.play.ui.explorewidget.adapter.delegate

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.databinding.ViewPlayGridBinding
import com.tokopedia.play.ui.explorewidget.viewholder.PlayExploreWidgetViewHolder
import com.tokopedia.play.widget.ui.adapter.viewholder.placeholder.PlayWidgetCardPlaceholderViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetChannelUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetItemUiModel
import com.tokopedia.play.widget.ui.model.PlayWidgetShimmerUiModel
import com.tokopedia.play.R as playR

/**
 * @author by astidhiyaa on 02/12/22
 */
class WidgetAdapterDelegate private constructor() {
    internal class Widget(
        private val listener: PlayExploreWidgetViewHolder.Widget.Listener
    ) : TypedAdapterDelegate<PlayWidgetChannelUiModel, PlayWidgetItemUiModel, PlayExploreWidgetViewHolder.Widget>(
        playR.layout.view_play_grid
    ) {

        override fun onBindViewHolder(
            item: PlayWidgetChannelUiModel,
            holder: PlayExploreWidgetViewHolder.Widget
        ) {
            holder.bind(item)
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            basicView: View
        ): PlayExploreWidgetViewHolder.Widget {
            val binding =
                ViewPlayGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return PlayExploreWidgetViewHolder.Widget.create(binding, listener)
        }
    }

    internal class Shimmering :
        TypedAdapterDelegate<PlayWidgetShimmerUiModel, PlayWidgetItemUiModel, PlayWidgetCardPlaceholderViewHolder>(
            PlayWidgetCardPlaceholderViewHolder.layout
        ) {

        override fun onBindViewHolder(
            item: PlayWidgetShimmerUiModel,
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
