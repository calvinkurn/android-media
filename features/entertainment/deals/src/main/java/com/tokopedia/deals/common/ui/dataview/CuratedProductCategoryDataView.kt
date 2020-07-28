package com.tokopedia.deals.common.ui.dataview

data class CuratedProductCategoryDataView(
        val title: String = "",
        val subtitle: String = "",
        val productCards: List<ProductCardDataView> = emptyList(),
        val hasSeeAllButton: Boolean = true,
        val seeAllUrl: String = ""
) : DealsBaseItemDataView()