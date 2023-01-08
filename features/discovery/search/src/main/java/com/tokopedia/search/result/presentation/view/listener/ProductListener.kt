package com.tokopedia.search.result.presentation.view.listener

import com.tokopedia.productcard.ProductCardLifecycleObserver
import com.tokopedia.search.result.presentation.model.ProductItemDataView

interface ProductListener {
    fun onItemClicked(item: ProductItemDataView?, adapterPosition: Int)
    fun onThreeDotsClick(item: ProductItemDataView?, adapterPosition: Int)
    fun onProductImpressed(item: ProductItemDataView?, adapterPosition: Int)
    fun onAddToCartClick(item: ProductItemDataView)
    val productCardLifecycleObserver: ProductCardLifecycleObserver?
}
