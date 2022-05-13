package com.tokopedia.createpost.producttag.view.uimodel


/**
 * Created By : Jonathan Darwin on May 10, 2022
 */
data class GlobalSearchProductUiModel(
    val products: List<ProductUiModel>,
    val quickFilters: List<QuickFilterUiModel>,
    val selectedQuickFilters: List<QuickFilterUiModel>,
    val nextCursor: Int,
    val state: PagedState,
    val query: String,
    val suggestion: String,
    val ticker: TickerUiModel,
) {

    companion object {
        val Empty = GlobalSearchProductUiModel(
            products = emptyList(),
            quickFilters = emptyList(),
            selectedQuickFilters = emptyList(),
            nextCursor = 0,
            state = PagedState.Unknown,
            query = "",
            suggestion = "",
            ticker = TickerUiModel()
        )
    }
}