package com.tokopedia.deals.common.listener

import com.tokopedia.deals.common.ui.dataview.ProductCardDataView

interface ProductListListener {
    fun onProductClicked(
        productCardDataView: ProductCardDataView,
        productItemPosition: Int
    )
    fun onImpressionProduct(
            productCardDataView: ProductCardDataView,
            productItemPosition: Int,
            page:Int
    )
}