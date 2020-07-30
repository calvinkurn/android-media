package com.tokopedia.carouselproductcard

internal class CarouselProductCardListenerInfo {
    
    var onItemClickListener: CarouselProductCardListener.OnItemClickListener? = null
        set(value) {
            if (value != null) {
                field = value
            }
        }

    var onItemImpressedListener: CarouselProductCardListener.OnItemImpressedListener? = null
        set(value) {
            if (value != null) {
                field = value
            }
        }

    var onItemAddToCartListener: CarouselProductCardListener.OnItemAddToCartListener? = null
        set(value) {
            if (value != null) {
                field = value
            }
        }

    var onItemThreeDotsClickListener: CarouselProductCardListener.OnItemThreeDotsClickListener? = null
        set(value) {
            if (value != null) {
                field = value
            }
        }
}