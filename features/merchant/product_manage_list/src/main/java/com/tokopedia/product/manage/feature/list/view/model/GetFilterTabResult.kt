package com.tokopedia.product.manage.feature.list.view.model

sealed class GetFilterTabResult(open val tabs: List<FilterTabUiModel>) {

    data class ShowFilterTab(
        override val tabs: List<FilterTabUiModel>
    ): GetFilterTabResult(tabs)

    data class UpdateFilterTab(
        override val tabs: List<FilterTabUiModel>
    ): GetFilterTabResult(tabs)

}