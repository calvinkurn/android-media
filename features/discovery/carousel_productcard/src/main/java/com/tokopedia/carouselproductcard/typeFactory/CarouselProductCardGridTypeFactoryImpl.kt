package com.tokopedia.carouselproductcard.typeFactory

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.carouselproductcard.*
import com.tokopedia.carouselproductcard.CarouselProductCardGridViewHolder
import com.tokopedia.carouselproductcard.CarouselProductCardModel
import com.tokopedia.carouselproductcard.CarouselSeeMoreCardGridViewHolder
import com.tokopedia.carouselproductcard.CarouselSeeMoreCardModel

internal class CarouselProductCardGridTypeFactoryImpl(
    private val internalListener: CarouselProductCardInternalListener,
) : CarouselProductCardTypeFactory{
    override fun type(carouselProductCardModel: CarouselProductCardModel) : Int{
        return CarouselProductCardGridViewHolder.LAYOUT
    }

    override fun type(carouselSeeMoreCardModel: CarouselSeeMoreCardModel) : Int {
        return CarouselSeeMoreCardGridViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseProductCardViewHolder<BaseCarouselCardModel> {
        val view = LayoutInflater
                .from(viewGroup.context)
                .inflate(viewType, viewGroup, false)
        return when(viewType){
            CarouselProductCardGridViewHolder.LAYOUT ->
                CarouselProductCardGridViewHolder(view, internalListener)
            else ->
                CarouselSeeMoreCardGridViewHolder(view)
        } as BaseProductCardViewHolder<BaseCarouselCardModel>
    }
}