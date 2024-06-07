package com.tokopedia.carouselproductcard

import com.tokopedia.carouselproductcard.typeFactory.CarouselProductCardTypeFactory
import com.tokopedia.productcard.ProductCardModel

internal data class CarouselProductCardModel(
        val productCardModel: ProductCardModel,
        var carouselProductCardListenerInfo: CarouselProductCardListenerInfo
) : BaseCarouselCardModel {
    fun getOnViewListener() = carouselProductCardListenerInfo.onViewListener
    fun getOnItemClickListener() = carouselProductCardListenerInfo.onItemClickListener
    fun getOnItemAddToCartListener() = carouselProductCardListenerInfo.onItemAddToCartListener
    fun getOnItemImpressedListener() = carouselProductCardListenerInfo.onItemImpressedListener
    fun getOnItemThreeDotsClickListener() = carouselProductCardListenerInfo.onItemThreeDotsClickListener
    fun getOnATCNonVariantClickListener() = carouselProductCardListenerInfo.onATCNonVariantClickListener
    fun getAddVariantClickListener() = carouselProductCardListenerInfo.onAddVariantClickListener
    fun getSeeOtherClickListener() = carouselProductCardListenerInfo.onSeeOtherProductClickListener

    override fun type(typeFactory: CarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(newItem: BaseCarouselCardModel): Boolean {
        return newItem is CarouselProductCardModel && productCardModel.productName == newItem.productCardModel.productName
    }

    override fun areContentsTheSame(newItem: BaseCarouselCardModel): Boolean {
        return false
    }

}
