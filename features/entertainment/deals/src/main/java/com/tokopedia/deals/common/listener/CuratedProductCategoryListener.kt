package com.tokopedia.deals.common.listener

import com.tokopedia.deals.common.ui.dataview.CuratedProductCategoryDataView
import com.tokopedia.deals.common.ui.dataview.ProductCardDataView

interface CuratedProductCategoryListener {
    fun onProductClicked(
        productCardDataView: ProductCardDataView,
        productItemPosition: Int,
        sectionTitle: String
    )

    fun onSeeAllProductClicked(
        curatedProductCategoryDataView: CuratedProductCategoryDataView,
        position: Int
    )

    fun onImpressionCuratedProduct(
            curatedProductCategoryDataView: CuratedProductCategoryDataView,
            position: Int
    )
}