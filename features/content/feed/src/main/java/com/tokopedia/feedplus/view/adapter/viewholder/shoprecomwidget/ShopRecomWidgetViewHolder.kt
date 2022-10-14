package com.tokopedia.feedplus.view.adapter.viewholder.shoprecomwidget

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.feedcomponent.shoprecom.ShopRecomWidgetCarousel
import com.tokopedia.feedcomponent.view.viewmodel.shoprecommendation.ShopRecomWidgetViewModel

/**
 * created by fachrizalmrsln on 14/10/22
 */
class ShopRecomWidgetViewHolder(
        private val shopRecomWidgetCarousel: ShopRecomWidgetCarousel
) : AbstractViewHolder<ShopRecomWidgetViewModel>(shopRecomWidgetCarousel.itemView) {

    override fun bind(element: ShopRecomWidgetViewModel) {
        shopRecomWidgetCarousel.bind(element.shopRecomUiModel)
    }

    override fun bind(element: ShopRecomWidgetViewModel, payloads: MutableList<Any>) {
        bind(element)
    }

    companion object {
        val LAYOUT = ShopRecomWidgetCarousel.layout
    }
}
