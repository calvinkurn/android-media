package com.tokopedia.tokomart.searchcategory.presentation.listener

import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView

interface ProductItemListener {

    fun onProductImpressed(productItemDataView: ProductItemDataView)

    fun onProductClick(productItemDataView: ProductItemDataView)

    fun onProductNonVariantQuantityChanged(productItemDataView: ProductItemDataView, quantity: Int)

    fun onProductChooseVariantClicked(productItemDataView: ProductItemDataView)
}