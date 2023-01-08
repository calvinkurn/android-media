package com.tokopedia.content.common.producttag.view.uimodel

import com.tokopedia.filter.common.data.DynamicFilterModel

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
data class GlobalSearchShopUiModel(
    val shops: List<ShopUiModel>,
    val quickFilters: List<QuickFilterUiModel>,
    val sortFilters: DynamicFilterModel,
    val state: PagedState,
    val param: SearchParamUiModel,
) {

    companion object {
        val Empty: GlobalSearchShopUiModel
            get() = GlobalSearchShopUiModel(
                shops = emptyList(),
                sortFilters = DynamicFilterModel(),
                quickFilters = emptyList(),
                state = PagedState.Unknown,
                param = SearchParamUiModel.Empty,
            )
    }
}