package com.tokopedia.carouselproductcard

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.v2.ProductCardModel

interface CarouselProductCardListener {

    interface OnItemClickListener {
        fun onItemClick(productCardModel: ProductCardModel, adapterPosition: Int)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(productCardModel: ProductCardModel, adapterPosition: Int)
    }

    interface OnItemImpressedListener {
        fun onItemImpressed(productCardModel: ProductCardModel, adapterPosition: Int)
        fun getImpressHolder(adapterPosition: Int): ImpressHolder
    }

    interface OnItemAddToCartListener {
        fun onItemAddToCart(productCardModel: ProductCardModel, adapterPosition: Int)
    }

    interface OnWishlistItemClickListener {
        fun onWishlistItemClick(productCardModel: ProductCardModel, adapterPosition: Int)
    }
}