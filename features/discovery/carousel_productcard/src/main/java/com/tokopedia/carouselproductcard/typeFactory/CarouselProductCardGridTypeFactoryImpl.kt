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
    private val isReimagine: Boolean,
) : CarouselProductCardTypeFactory{
    override fun type(carouselProductCardModel: CarouselProductCardModel) : Int{
        return CarouselProductCardGridViewHolder.layout(isReimagine)
    }

    override fun type(carouselSeeMoreCardModel: CarouselSeeMoreCardModel) : Int {
        return CarouselSeeMoreCardGridViewHolder.LAYOUT
    }

    override fun type(carouselViewAllCardModel: CarouselViewAllCardModel): Int {
        return CarouselViewAllCardViewHolder.LAYOUT
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): BaseProductCardViewHolder<BaseCarouselCardModel> {
        val view = LayoutInflater
                .from(viewGroup.context)
                .inflate(viewType, viewGroup, false)
        return when(viewType){
            CarouselProductCardGridViewHolder.layout(isReimagine) ->
                CarouselProductCardGridViewHolder(view, internalListener)
            CarouselViewAllCardViewHolder.LAYOUT ->
                CarouselViewAllCardViewHolder(view)
            else ->
                CarouselSeeMoreCardGridViewHolder(view)
        } as BaseProductCardViewHolder<BaseCarouselCardModel>
    }
}
