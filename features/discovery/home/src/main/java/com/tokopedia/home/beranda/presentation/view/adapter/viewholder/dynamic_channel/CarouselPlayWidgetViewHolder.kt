package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.dynamic_channel

import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.CarouselPlayWidgetDataModel
import com.tokopedia.play.widget.PlayWidgetViewHolder


/**
 * Created by mzennis on 19/10/20.
 */
class CarouselPlayWidgetViewHolder(
        private val widgetViewHolder: PlayWidgetViewHolder
) : AbstractViewHolder<CarouselPlayWidgetDataModel>(widgetViewHolder.itemView) {


    override fun bind(element: CarouselPlayWidgetDataModel?) {
        element?.let { item ->
            widgetViewHolder.bind(item.widgetUiModel)
        }
    }

    companion object {
        @LayoutRes val LAYOUT = PlayWidgetViewHolder.layout
    }
}