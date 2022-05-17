package com.tokopedia.createpost.producttag.view.uimodel


/**
 * Created By : Jonathan Darwin on May 10, 2022
 */
data class GlobalSearchProductUiModel(
    val products: List<ProductUiModel>,
    val quickFilters: List<QuickFilterUiModel>,
    val nextCursor: Int,
    val state: PagedState,
    val param: SearchParamUiModel,
    val suggestion: String,
    val ticker: TickerUiModel,
) {

    companion object {
        val Empty = GlobalSearchProductUiModel(
            products = emptyList(),
            quickFilters = emptyList(),
            nextCursor = 0,
            state = PagedState.Unknown,
            param = SearchParamUiModel.Empty,
            suggestion = "",
            ticker = TickerUiModel()
        )
    }
}