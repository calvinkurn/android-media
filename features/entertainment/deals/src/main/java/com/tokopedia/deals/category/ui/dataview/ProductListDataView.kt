package com.tokopedia.deals.category.ui.dataview

import com.tokopedia.deals.common.ui.dataview.DealsBaseItemDataView
import com.tokopedia.deals.common.ui.dataview.ProductCardDataView

data class ProductListDataView(val productCards: MutableList<ProductCardDataView> = mutableListOf(), val page:Int = 0)
    : DealsBaseItemDataView()