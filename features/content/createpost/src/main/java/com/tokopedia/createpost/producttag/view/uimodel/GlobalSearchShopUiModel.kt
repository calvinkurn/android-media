package com.tokopedia.createpost.producttag.view.uimodel

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
data class GlobalSearchShopUiModel(
    val shops: List<ShopUiModel>,
    val quickFilters: List<QuickFilterUiModel>,
    val nextCursor: Int,
    val state: PagedState,
    val param: SearchParamUiModel,
) {

    companion object {
        val Empty = GlobalSearchShopUiModel(
            shops = emptyList(),
            quickFilters = emptyList(),
            nextCursor = 0,
            state = PagedState.Unknown,
            param = SearchParamUiModel.Empty,
        )
    }
}