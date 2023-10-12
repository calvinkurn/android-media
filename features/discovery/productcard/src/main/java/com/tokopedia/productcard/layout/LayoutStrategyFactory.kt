package com.tokopedia.productcard.layout

import com.tokopedia.productcard.ProductCardModel.ProductListType
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategy
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategyBorder
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategyClear
import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategyControl
import com.tokopedia.productcard.layout.stockbar.StockBarLayoutStrategy
import com.tokopedia.productcard.layout.stockbar.StockBarLayoutStrategyControl
import com.tokopedia.productcard.layout.stockbar.StockBarLayoutStrategyTop
import com.tokopedia.unifycomponents.CardUnify2.Companion.TYPE_BORDER
import com.tokopedia.unifycomponents.CardUnify2.Companion.TYPE_CLEAR

internal object LayoutStrategyFactory {

    fun create(
        productListType: ProductListType,
        isTopStockBar: Boolean,
        cardType: Int,
    ) : LayoutStrategy =
        productListTypeStrategy(
            productListType,
            stockBarLayoutStrategy(isTopStockBar),
            shadowLayoutStrategy(cardType),
        )

    private fun stockBarLayoutStrategy(useTopStockBar: Boolean) =
        if (useTopStockBar) StockBarLayoutStrategyTop() else StockBarLayoutStrategyControl()

    private fun shadowLayoutStrategy(cardType: Int) =
        when (cardType) {
            TYPE_BORDER -> ShadowLayoutStrategyBorder()
            TYPE_CLEAR -> ShadowLayoutStrategyClear()
            else -> ShadowLayoutStrategyControl()
        }

    private fun productListTypeStrategy(
        productListType: ProductListType,
        stockBarLayoutStrategy: StockBarLayoutStrategy,
        shadowLayoutStrategy: ShadowLayoutStrategy,
    ) = when (productListType) {
            ProductListType.CONTROL -> LayoutStrategyControl(
                stockBarLayoutStrategy,
                shadowLayoutStrategy,
            )
            ProductListType.REPOSITION -> LayoutStrategyFashionReposition()
            ProductListType.LONG_IMAGE -> LayoutStrategyFashionLongImage()
            ProductListType.GIMMICK -> LayoutStrategyReposition()
            ProductListType.PORTRAIT -> LayoutStrategyPortrait()
            ProductListType.ETA -> LayoutStrategyEta()
            ProductListType.BEST_SELLER -> LayoutStrategyBestSeller()
            ProductListType.FIXED_GRID -> LayoutStrategyFixedGrid()
            ProductListType.LIST_VIEW -> LayoutStrategyListView()
        }
}
