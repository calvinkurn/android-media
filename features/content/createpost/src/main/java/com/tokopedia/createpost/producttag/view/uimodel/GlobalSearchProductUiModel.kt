package com.tokopedia.createpost.producttag.view.uimodel

import kotlinx.coroutines.channels.ticker

/**
 * Created By : Jonathan Darwin on May 10, 2022
 */
data class GlobalSearchProductUiModel(
    val products: List<ProductUiModel>,
    val nextCursor: Int,
    val state: PagedState,
    val query: String,
    val suggestion: String,
    val ticker: TickerUiModel,
) {

    companion object {
        val Empty = GlobalSearchProductUiModel(
            products = emptyList(),
            nextCursor = 0,
            state = PagedState.Unknown,
            query = "",
            suggestion = "",
            ticker = TickerUiModel()
        )
    }
}