package com.tokopedia.feedplus.oldFeed.view.adapter.viewholder.carouselplaycard

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardModel
import com.tokopedia.play.widget.PlayWidgetViewHolder

/**
 * Created by jegul on 08/10/20
 */
class CarouselPlayCardViewHolder(
        private val playWidgetViewHolder: PlayWidgetViewHolder
) : AbstractViewHolder<CarouselPlayCardModel>(playWidgetViewHolder.itemView) {

    override fun bind(element: CarouselPlayCardModel) {
        playWidgetViewHolder.bind(element.playWidgetState, this)
    }

    override fun bind(element: CarouselPlayCardModel, payloads: MutableList<Any>) {
        bind(element)
    }

    companion object {
        val LAYOUT = PlayWidgetViewHolder.layout
    }
}
