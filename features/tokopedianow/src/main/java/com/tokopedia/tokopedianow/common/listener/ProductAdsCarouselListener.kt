package com.tokopedia.tokopedianow.common.listener

import com.tokopedia.productcard.compact.productcardcarousel.presentation.uimodel.ProductCardCompactCarouselItemUiModel

interface ProductAdsCarouselListener {

    fun onProductCardClicked(
        position: Int,
        title: String,
        product: ProductCardCompactCarouselItemUiModel
    )
    fun onProductCardImpressed(
        position: Int,
        title: String,
        product: ProductCardCompactCarouselItemUiModel
    )
    fun onProductCardQuantityChanged(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel,
        quantity: Int
    )
    fun onProductCardAddVariantClicked(
        position: Int,
        product: ProductCardCompactCarouselItemUiModel
    )

    fun onProductCardAddToCartBlocked()
}
