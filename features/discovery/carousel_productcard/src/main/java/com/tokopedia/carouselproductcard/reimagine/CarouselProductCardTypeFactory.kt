package com.tokopedia.carouselproductcard.reimagine

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.carouselproductcard.reimagine.grid.CarouselProductCardGridModel
import com.tokopedia.carouselproductcard.reimagine.viewallcard.CarouselProductCardViewAllCardModel

interface CarouselProductCardTypeFactory {

    fun type(model: CarouselProductCardGridModel): Int

    fun type(model: CarouselProductCardViewAllCardModel): Int

    fun onCreateViewHolder(parent: View, type: Int): AbstractViewHolder<*>
}
