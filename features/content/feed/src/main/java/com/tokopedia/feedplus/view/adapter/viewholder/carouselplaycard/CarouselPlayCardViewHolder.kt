package com.tokopedia.feedplus.view.adapter.viewholder.carouselplaycard

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.view.viewmodel.carousel.CarouselPlayCardViewModel
import com.tokopedia.play.widget.PlayWidgetViewHolder

/**
 * Created by jegul on 08/10/20
 */
class CarouselPlayCardViewHolder(
        private val playWidgetViewHolder: PlayWidgetViewHolder
) : AbstractViewHolder<CarouselPlayCardViewModel>(playWidgetViewHolder.itemView) {

    override fun bind(element: CarouselPlayCardViewModel) {
        playWidgetViewHolder.bind(element.playWidgetUiModel, this)
    }

    override fun bind(element: CarouselPlayCardViewModel, payloads: MutableList<Any>) {
        bind(element)
    }

    companion object {
        val LAYOUT = PlayWidgetViewHolder.layout
    }
}