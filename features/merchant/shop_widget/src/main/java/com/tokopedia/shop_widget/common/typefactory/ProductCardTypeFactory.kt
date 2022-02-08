package com.tokopedia.shop_widget.common.typefactory

import com.tokopedia.shop_widget.common.uimodel.ProductCardUiModel

interface ProductCardTypeFactory {
    fun type(uiModel: ProductCardUiModel): Int
}