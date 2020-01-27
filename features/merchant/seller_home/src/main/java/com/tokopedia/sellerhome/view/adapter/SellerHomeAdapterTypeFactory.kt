package com.tokopedia.sellerhome.view.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhome.view.model.CardWidgetUiModel
import com.tokopedia.sellerhome.view.model.CarouselWidgetUiModel
import com.tokopedia.sellerhome.view.model.LineGraphWidgetUiModel
import com.tokopedia.sellerhome.view.viewholder.CardViewHolder
import com.tokopedia.sellerhome.view.viewholder.CarouselViewHolder
import com.tokopedia.sellerhome.view.viewholder.LineGraphViewHolder

/**
 * Created By @ilhamsuaib on 2020-01-14
 */

class SellerHomeAdapterTypeFactory : BaseAdapterTypeFactory(), SellerHomeTypeFactory {

    override fun type(cardWidget: CardWidgetUiModel): Int {
        return CardViewHolder.RES_LAYOUT
    }

    override fun type(lineGraphWidget: LineGraphWidgetUiModel): Int {
        return LineGraphViewHolder.RES_LAYOUT
    }

    override fun type(carouselWidgetUiModel: CarouselWidgetUiModel): Int {
        return CarouselViewHolder.RES_LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            CardViewHolder.RES_LAYOUT -> CardViewHolder(parent)
            LineGraphViewHolder.RES_LAYOUT -> LineGraphViewHolder(parent)
            CarouselViewHolder.RES_LAYOUT -> CarouselViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}