package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView

interface ProductItemListener {

    fun onProductImpressed(productItemDataView: ProductItemDataView)

    fun onProductClick(productItemDataView: ProductItemDataView)

    fun onProductNonVariantQuantityChanged(productItemDataView: ProductItemDataView, quantity: Int)

    fun onProductChooseVariantClicked(productItemDataView: ProductItemDataView)
}