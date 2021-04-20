package com.tokopedia.shop.home.view.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel

/**
 * Created by mzennis on 13/10/20.
 */
class CarouselPlayWidgetViewHolder(
        private val playWidgetViewHolder: PlayWidgetViewHolder
) : AbstractViewHolder<CarouselPlayWidgetUiModel>(playWidgetViewHolder.itemView) {

    override fun bind(element: CarouselPlayWidgetUiModel) {
        playWidgetViewHolder.bind(element.widgetUiModel, this)
    }

    companion object {
        val LAYOUT = R.layout.item_shop_home_play_widget
    }
}