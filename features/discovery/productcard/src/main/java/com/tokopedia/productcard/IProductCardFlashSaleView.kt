package com.tokopedia.productcard

interface IProductCardFlashSaleView {

    fun setProductModel(productCardModel: ProductCardFlashSaleModel)

    fun getCardMaxElevation(): Float

    fun getCardRadius(): Float

    fun recycle()
}