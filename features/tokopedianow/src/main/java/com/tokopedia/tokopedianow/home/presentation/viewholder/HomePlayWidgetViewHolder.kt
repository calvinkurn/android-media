package com.tokopedia.tokopedianow.home.presentation.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomePlayWidgetUiModel

class HomePlayWidgetViewHolder(
    private val playWidgetViewHolder: PlayWidgetViewHolder?
): AbstractViewHolder<HomePlayWidgetUiModel>(playWidgetViewHolder?.itemView) {

    companion object {
        val LAYOUT = PlayWidgetViewHolder.layout
    }

    override fun bind(playWidget: HomePlayWidgetUiModel) {
        playWidgetViewHolder?.bind(playWidget.playWidgetState)
    }
}