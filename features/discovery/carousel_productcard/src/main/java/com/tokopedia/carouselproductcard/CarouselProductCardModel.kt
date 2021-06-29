package com.tokopedia.carouselproductcard

import com.tokopedia.carouselproductcard.typeFactory.CarouselProductCardTypeFactory
import com.tokopedia.productcard.ProductCardModel

internal data class CarouselProductCardModel(
        val productCardModel: ProductCardModel,
        var carouselProductCardListenerInfo: CarouselProductCardListenerInfo
) : BaseCarouselCardModel {
    fun getOnItemClickListener() = carouselProductCardListenerInfo.onItemClickListener
    fun getOnItemAddToCartListener() = carouselProductCardListenerInfo.onItemAddToCartListener
    fun getOnItemImpressedListener() = carouselProductCardListenerInfo.onItemImpressedListener
    fun getOnItemThreeDotsClickListener() = carouselProductCardListenerInfo.onItemThreeDotsClickListener
    fun getOnAddToCartNonVariantClickListener() = carouselProductCardListenerInfo.onAddToCartNonVariantClickListener
    fun getOnAddVariantClickListener() = carouselProductCardListenerInfo.onAddVariantClickListener

    override fun areItemsTheSame(newItem: BaseCarouselCardModel): Boolean {
        return newItem is CarouselProductCardModel && productCardModel.productName == newItem.productCardModel.productName
    }

    override fun type(typeFactory: CarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areContentsTheSame(newItem: BaseCarouselCardModel): Boolean {
        return newItem is CarouselProductCardModel && productCardModel == newItem.productCardModel
    }
}