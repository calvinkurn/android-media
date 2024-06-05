package com.tokopedia.carouselproductcard

internal class CarouselProductCardListenerInfo {
    
    var onItemClickListener: CarouselProductCardListener.OnItemClickListener? = null
        set(value) {
            if (value != null) {
                field = value
            }
        }

    var onViewListener: CarouselProductCardListener.OnViewListener? = null
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

    var onATCNonVariantClickListener: CarouselProductCardListener.OnATCNonVariantClickListener? = null
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

    var onSeeOtherProductClickListener: CarouselProductCardListener.OnSeeOtherProductClickListener? = null
        set(value) {
            if (value != null) {
                field = value
            }
        }

    var onViewAllCardClickListener: CarouselProductCardListener.OnViewAllCardClickListener? = null
        set(value) {
            if (value != null) {
                field = value
            }
        }
}
