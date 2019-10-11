package com.tokopedia.productcard.carousel

internal class ProductCardCarouselListenerInfo {
    
     var onItemClickListener: ProductCardCarouselListener.OnItemClickListener? = null
     var onItemLongClickListener: ProductCardCarouselListener.OnItemLongClickListener? = null
     var onItemImpressedListener: ProductCardCarouselListener.OnItemImpressedListener? = null
     var onItemAddToCartListener: ProductCardCarouselListener.OnItemAddToCartListener? = null
     var onWishlistItemClickListener: ProductCardCarouselListener.OnWishlistItemClickListener? = null
}