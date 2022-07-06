package com.tokopedia.carouselproductcard.typeFactory

import android.view.View
import android.view.ViewGroup
import com.tokopedia.carouselproductcard.BaseCarouselCardModel
import com.tokopedia.carouselproductcard.BaseProductCardViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardModel
import com.tokopedia.carouselproductcard.CarouselSeeMoreCardModel
import com.tokopedia.carouselproductcard.CarouselViewAllCardModel

internal interface CarouselProductCardTypeFactory {
    fun type(carouselProductCardModel: CarouselProductCardModel): Int
    fun type(carouselSeeMoreCardModel: CarouselSeeMoreCardModel): Int
    fun type(carouselViewAllCardModel: CarouselViewAllCardModel): Int
    fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseProductCardViewHolder<BaseCarouselCardModel>
}