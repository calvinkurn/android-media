package com.tokopedia.content.common.producttag.view.uimodel

import com.tokopedia.filter.common.data.DynamicFilterModel

/**
 * Created By : Jonathan Darwin on May 10, 2022
 */
data class GlobalSearchProductUiModel(
    val products: List<ProductUiModel>,
    val quickFilters: List<QuickFilterUiModel>,
    val sortFilters: DynamicFilterModel,
    val state: PagedState,
    val param: SearchParamUiModel,
    val suggestion: SuggestionUiModel,
    val ticker: TickerUiModel,
) {

    companion object {
        val Empty: GlobalSearchProductUiModel
            get() = GlobalSearchProductUiModel(
                products = emptyList(),
                quickFilters = emptyList(),
                sortFilters = DynamicFilterModel(),
                state = PagedState.Unknown,
                param = SearchParamUiModel.Empty,
                suggestion = SuggestionUiModel(),
                ticker = TickerUiModel()
            )
    }
}