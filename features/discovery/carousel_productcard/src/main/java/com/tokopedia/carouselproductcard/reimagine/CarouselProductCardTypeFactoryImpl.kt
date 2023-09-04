package com.tokopedia.carouselproductcard.reimagine

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.reimagine.grid.CarouselProductCardGridModel
import com.tokopedia.carouselproductcard.reimagine.grid.CarouselProductCardGridViewHolder
import com.tokopedia.carouselproductcard.reimagine.viewallcard.CarouselProductCardViewAllCardModel
import com.tokopedia.carouselproductcard.reimagine.viewallcard.CarouselProductCardViewAllCardViewHolder

internal class CarouselProductCardTypeFactoryImpl: CarouselProductCardTypeFactory {

    override fun type(model: CarouselProductCardGridModel): Int =
        CarouselProductCardGridViewHolder.LAYOUT

    override fun type(model: CarouselProductCardViewAllCardModel): Int =
        CarouselProductCardViewAllCardViewHolder.LAYOUT

    override fun onCreateViewHolder(parent: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            CarouselProductCardGridViewHolder.LAYOUT -> CarouselProductCardGridViewHolder(parent)
            CarouselProductCardViewAllCardViewHolder.LAYOUT ->
                CarouselProductCardViewAllCardViewHolder(parent)
            else -> throw TypeNotSupportedException.create("Layout not supported")
        }
    }
}
