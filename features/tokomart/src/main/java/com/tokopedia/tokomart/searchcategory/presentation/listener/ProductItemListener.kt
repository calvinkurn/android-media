package com.tokopedia.tokomart.searchcategory.presentation.listener

import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView

interface ProductItemListener {

    fun onProductNonVariantQuantityChanged(productItemDataView: ProductItemDataView, quantity: Int)

    fun onProductChooseVariantClicked(productItemDataView: ProductItemDataView)
}