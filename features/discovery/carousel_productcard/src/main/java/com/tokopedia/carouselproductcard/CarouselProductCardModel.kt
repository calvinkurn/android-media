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
    fun getOnATCNonVariantClickListener() = carouselProductCardListenerInfo.onATCNonVariantClickListener
    fun getAddVariantClickListener() = carouselProductCardListenerInfo.onAddVariantClickListener

    override fun type(typeFactory: CarouselProductCardTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(newItem: BaseCarouselCardModel): Boolean {
        return newItem is CarouselProductCardModel
                && productCardModel.productName == newItem.productCardModel.productName
    }

    override fun areContentsTheSame(newItem: BaseCarouselCardModel): Boolean {
        if (newItem !is CarouselProductCardModel) return false
        val newProductCardModel = newItem.productCardModel

        return productCardModel.productName == newProductCardModel.productName
                && productCardModel.productImageUrl == newProductCardModel.productImageUrl
                && productCardModel.formattedPrice == newProductCardModel.formattedPrice
                && productCardModel.nonVariant?.quantity == newProductCardModel.nonVariant?.quantity
                && productCardModel.variant?.quantity == newProductCardModel.variant?.quantity
    }

}
