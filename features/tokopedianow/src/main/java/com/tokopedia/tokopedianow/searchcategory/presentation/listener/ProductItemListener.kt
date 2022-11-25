package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokopedianow.similarproduct.model.SimilarProductUiModel

interface ProductItemListener {

    fun onProductImpressed(productItemDataView: ProductItemDataView)

    fun onProductClick(productItemDataView: ProductItemDataView)

    fun onProductNonVariantQuantityChanged(productItemDataView: ProductItemDataView, quantity: Int)

    fun onProductChooseVariantClicked(productItemDataView: ProductItemDataView)

    fun trackClickSimilarProductBtn(productId: String)

    fun trackImpressionBottomSheet(userId: String, warehouseId: String, productId: String, similarProducts: ArrayList<SimilarProductUiModel>)

    fun trackClickProduct(userId: String, warehouseId: String, productId: String, similarProducts: ArrayList<SimilarProductUiModel>)

    fun trackClickAddToCart(userId: String, warehouseId: String, product: SimilarProductUiModel, similarProducts: ArrayList<SimilarProductUiModel>)

    fun trackClickCloseBottomsheet(warehouseId: String, productId: String, similarProducts: ArrayList<SimilarProductUiModel>)

    fun trackImpressionEmptyState(warehouseId: String, productId: String)

}
