package com.tokopedia.search.result.product.addtocart

import com.tokopedia.search.result.presentation.model.ProductItemDataView

interface AddToCartPresenter {
    val productAddedToCart: ProductItemDataView

    fun addToCart(data: ProductItemDataView?)
}
