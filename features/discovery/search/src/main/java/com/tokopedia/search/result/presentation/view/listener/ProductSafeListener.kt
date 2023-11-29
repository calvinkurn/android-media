package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.search.result.presentation.model.ProductItemDataView

interface ProductSafeListener {
    fun onSafeProductClickInfo(item: ProductItemDataView?, adapterPosition: Int)
}
