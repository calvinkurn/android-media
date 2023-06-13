package com.tokopedia.productcard.layout

import com.tokopedia.productcard.ProductCardModel

internal object LayoutStrategyFactory {
    fun create(
        productListType: ProductCardModel.ProductListType
    ) : LayoutStrategy {
        return when (productListType) {
            ProductCardModel.ProductListType.CONTROL -> LayoutStrategyControl()
            ProductCardModel.ProductListType.REPOSITION -> LayoutStrategyFashionReposition()
            ProductCardModel.ProductListType.LONG_IMAGE -> LayoutStrategyFashionLongImage()
            ProductCardModel.ProductListType.GIMMICK -> LayoutStrategyReposition()
            ProductCardModel.ProductListType.PORTRAIT -> LayoutStrategyPortrait()
            ProductCardModel.ProductListType.ETA -> LayoutStrategyEta()
        }
    }
}
