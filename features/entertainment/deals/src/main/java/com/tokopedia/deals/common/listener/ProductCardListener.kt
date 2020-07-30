package com.tokopedia.deals.common.listener

import android.view.View
import com.tokopedia.deals.common.ui.dataview.ProductCardDataView

interface ProductCardListener {
    fun onProductClicked(itemView: View, productCardDataView: ProductCardDataView, position: Int)
    fun onImpressionProduct(
            productCardDataView: ProductCardDataView,
            productItemPosition: Int,
            page:Int
    )
}