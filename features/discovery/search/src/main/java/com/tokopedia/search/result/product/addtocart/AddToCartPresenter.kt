package com.tokopedia.search.result.product.addtocart

import com.tokopedia.search.result.product.inspirationcarousel.InspirationCarouselDataView

interface AddToCartPresenter {
    val productAddedToCart: InspirationCarouselDataView.Option.Product

    fun addToCart(addToCartData: AddToCartData?)
}
