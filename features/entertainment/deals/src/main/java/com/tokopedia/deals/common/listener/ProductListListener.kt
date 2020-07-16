package com.tokopedia.deals.common.listener

import com.tokopedia.deals.common.ui.dataview.ProductCardDataView

interface ProductListListener {
    fun onProductClicked(
        productCardDataView: ProductCardDataView,
        productItemPosition: Int
    )
}