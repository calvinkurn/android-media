package com.tokopedia.productcard.layout.stockbar

import android.view.ViewGroup
import com.tokopedia.productcard.ProductCardModel

internal interface StockBarLayoutStrategy {

    fun renderStockBar(
        productCardModel: ProductCardModel,
        productCardViewContainer: ViewGroup,
    )
}
