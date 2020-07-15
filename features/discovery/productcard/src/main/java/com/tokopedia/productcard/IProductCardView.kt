package com.tokopedia.productcard

interface IProductCardView {

    fun setProductModel(productCardModel: ProductCardModel)

    fun getCardMaxElevation(): Float

    fun getCardRadius(): Float

    fun recycle()
}