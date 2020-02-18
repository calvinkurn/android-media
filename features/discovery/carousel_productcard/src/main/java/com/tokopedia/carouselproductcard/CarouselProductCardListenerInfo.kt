package com.tokopedia.carouselproductcard

internal class CarouselProductCardListenerInfo {
    
     var onItemClickListener: CarouselProductCardListener.OnItemClickListener? = null
     @Deprecated("Cannot long press anymore") var onItemLongClickListener: CarouselProductCardListener.OnItemLongClickListener? = null
     var onItemImpressedListener: CarouselProductCardListener.OnItemImpressedListener? = null
     var onItemAddToCartListener: CarouselProductCardListener.OnItemAddToCartListener? = null
     @Deprecated("Cannot wishlist anymore") var onWishlistItemClickListener: CarouselProductCardListener.OnWishlistItemClickListener? = null
}