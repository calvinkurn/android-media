package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.unifycomponents.Toaster

interface ProductItemListener {

    fun onProductImpressed(productItemDataView: ProductItemDataView)

    fun onProductClick(productItemDataView: ProductItemDataView)

    fun onProductNonVariantQuantityChanged(productItemDataView: ProductItemDataView, quantity: Int)

    fun onProductChooseVariantClicked(productItemDataView: ProductItemDataView)

    fun onWishlistButtonClicked(productId: String, isWishlistSelected: Boolean, descriptionToaster: String, ctaToaster: String, type: Int = Toaster.TYPE_NORMAL, ctaClickListener: (() -> Unit)? = null)
}
