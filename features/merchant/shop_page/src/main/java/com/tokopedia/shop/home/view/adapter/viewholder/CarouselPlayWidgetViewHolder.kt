package com.tokopedia.shop.home.view.adapter.viewholder

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.home.view.listener.ShopHomePlayWidgetListener
import com.tokopedia.shop.home.view.model.CarouselPlayWidgetUiModel

/**
 * Created by mzennis on 13/10/20.
 */
class CarouselPlayWidgetViewHolder(
        private val playWidgetViewHolder: PlayWidgetViewHolder,
        private val shopHomePlayWidgetListener: ShopHomePlayWidgetListener
) : AbstractViewHolder<CarouselPlayWidgetUiModel>(playWidgetViewHolder.itemView) {

    override fun bind(element: CarouselPlayWidgetUiModel) {
        playWidgetViewHolder.bind(element.playWidgetState, this)
        setWidgetImpressionListener(element)
    }

    private fun setWidgetImpressionListener(model: CarouselPlayWidgetUiModel) {
        itemView.addOnImpressionListener(model.impressHolder) {
            shopHomePlayWidgetListener.onPlayWidgetImpression(model, adapterPosition)
        }
    }

    companion object {
        val LAYOUT = R.layout.item_shop_home_play_widget
    }
}