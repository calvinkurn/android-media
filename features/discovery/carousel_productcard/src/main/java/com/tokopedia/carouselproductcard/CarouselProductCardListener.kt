package com.tokopedia.carouselproductcard

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel

interface CarouselProductCardListener {

    interface OnItemClickListener {
        fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int)
    }

    interface OnItemImpressedListener {
        fun onItemImpressed(productCardModel: ProductCardModel, carouselProductCardPosition: Int)
        fun getImpressHolder(carouselProductCardPosition: Int): ImpressHolder?
    }

    interface OnItemAddToCartListener {
        fun onItemAddToCart(productCardModel: ProductCardModel, carouselProductCardPosition: Int)
    }

    interface OnItemThreeDotsClickListener {
        fun onItemThreeDotsClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int)
    }

    interface OnSeeMoreClickListener{
        fun onSeeMoreClick()
    }

    interface OnAddToCartNonVariantClickListener{
        fun onAddToCartNonVariantClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int, quantity: Int)
    }

    interface OnAddVariantClickListener{
        fun onAddVariantClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int)
    }
}