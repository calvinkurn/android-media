package com.tokopedia.carouselproductcard

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel

interface CarouselProductCardListener {

    interface OnItemClickListener {
        fun onItemClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int)
        fun onAreaClicked(productCardModel: ProductCardModel, bindingAdapterPosition: Int) {}
        fun onProductImageClicked(productCardModel: ProductCardModel, bindingAdapterPosition: Int) {}
        fun onSellerInfoClicked(productCardModel: ProductCardModel, bindingAdapterPosition: Int) {}
    }

    interface OnViewListener {
        fun onViewAttachedToWindow(productCardModel: ProductCardModel, carouselProductCardPosition: Int)
        fun onViewDetachedFromWindow(productCardModel: ProductCardModel, carouselProductCardPosition: Int, visiblePercentage: Int)
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

    interface OnATCNonVariantClickListener {
        fun onATCNonVariantClick(
                productCardModel: ProductCardModel,
                carouselProductCardPosition: Int,
                quantity: Int,
        )
    }

    interface OnAddVariantClickListener {
        fun onAddVariantClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int)
    }

    interface OnSeeOtherProductClickListener {
        fun onSeeOtherProductClick(productCardModel: ProductCardModel, carouselProductCardPosition: Int)
    }

    interface OnViewAllCardClickListener {
        fun onViewAllCardClick()
    }
}
