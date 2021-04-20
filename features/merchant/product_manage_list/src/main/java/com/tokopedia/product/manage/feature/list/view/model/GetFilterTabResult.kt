package com.tokopedia.product.manage.feature.list.view.model

sealed class GetFilterTabResult(
    open val tabs: List<FilterTabUiModel>,
    open val totalProductCount: Int
) {
    data class ShowFilterTab(
        override val tabs: List<FilterTabUiModel>,
        override val totalProductCount: Int
    ): GetFilterTabResult(tabs, totalProductCount)

    data class UpdateFilterTab(
        override val tabs: List<FilterTabUiModel>,
        override val totalProductCount: Int
    ): GetFilterTabResult(tabs, totalProductCount)
}