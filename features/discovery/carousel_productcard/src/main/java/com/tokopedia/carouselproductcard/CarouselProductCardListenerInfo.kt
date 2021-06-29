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

    var onSeeMoreClickListener: CarouselProductCardListener.OnSeeMoreClickListener? = null
        set(value) {
            if (value != null) {
                field = value
            }
        }

    var onAddToCartNonVariantClickListener: CarouselProductCardListener.OnAddToCartNonVariantClickListener? = null
        set(value) {
            if (value != null) {
                field = value
            }
        }

    var onAddVariantClickListener: CarouselProductCardListener.OnAddVariantClickListener? = null
        set(value) {
            if (value != null) {
                field = value
            }
        }
}