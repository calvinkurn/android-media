package com.tokopedia.search.result.presentation.model

import com.tokopedia.search.result.domain.model.SearchProductModel.InspirationCarouselStockBar

data class StockBarDataView(
    val stock: Int = 0,
    val originalStock: Int = 0,
    val percentageValue: Int = 0,
    val value: String = "",
    val color: String = "",
) {
    companion object {
        fun create(stockBarModel: InspirationCarouselStockBar): StockBarDataView {
            return StockBarDataView(
                stockBarModel.stock,
                stockBarModel.originalStock,
                stockBarModel.percentageValue,
                stockBarModel.value,
                stockBarModel.color,
            )
        }
    }
}
