package com.tokopedia.productcard

import android.view.View

interface IProductCardView {

    fun setProductModel(productCardModel: ProductCardModel)

    fun getCardMaxElevation(): Float

    fun getCardRadius(): Float

    fun recycle()

    fun getThreeDotsButton(): View?
}