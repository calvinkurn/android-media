package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.productcard.ProductCardModel

data class HomeProductCardUiModel (
    val id: String,
    val parentProductId: String,
    val shopId: String,
    var productCardModel: ProductCardModel = ProductCardModel(),
)