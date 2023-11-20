package com.tokopedia.productcard.layout

import com.tokopedia.productcard.layout.shadow.ShadowLayoutStrategy
import com.tokopedia.productcard.layout.stockbar.StockBarLayoutStrategy

internal class LayoutStrategyControl(
    stockBarLayoutStrategy: StockBarLayoutStrategy,
    shadowLayoutStrategy: ShadowLayoutStrategy,
) : BaseLayoutStrategy(
    stockBarLayoutStrategy = stockBarLayoutStrategy,
    shadowLayoutStrategy = shadowLayoutStrategy,
)
