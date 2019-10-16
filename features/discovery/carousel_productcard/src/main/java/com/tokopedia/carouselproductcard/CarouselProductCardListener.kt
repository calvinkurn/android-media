package com.tokopedia.carouselproductcard

import com.tokopedia.productcard.v2.ProductCardModel

interface CarouselProductCardListener {

    interface OnItemClickListener {
        fun onItemClick(productCardModel: ProductCardModel)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(productCardModel: ProductCardModel)
    }

    interface OnItemImpressedListener {
        fun onItemImpressed(productCardModel: ProductCardModel)
    }

    interface OnItemAddToCartListener {
        fun onItemAddToCart(productCardModel: ProductCardModel)
    }

    interface OnWishlistItemClickListener {
        fun onWishlistItemClick(productCardModel: ProductCardModel)
    }
}