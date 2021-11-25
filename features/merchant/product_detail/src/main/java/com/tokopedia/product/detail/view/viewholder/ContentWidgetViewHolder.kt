package com.tokopedia.product.detail.view.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.ui.model.PlayWidgetUiModel
import com.tokopedia.product.detail.data.model.datamodel.ContentWidgetDataModel

class ContentWidgetViewHolder(
    private val playWidgetViewHolder: PlayWidgetViewHolder
) : AbstractViewHolder<ContentWidgetDataModel>(playWidgetViewHolder.itemView) {

    companion object {
        val LAYOUT = PlayWidgetViewHolder.layout
    }

    override fun bind(element: ContentWidgetDataModel) {
        playWidgetViewHolder.bind(element.playWidgetUiModel, this)
    }
}