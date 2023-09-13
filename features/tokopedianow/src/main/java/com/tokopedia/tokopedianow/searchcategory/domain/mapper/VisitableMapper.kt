package com.tokopedia.tokopedianow.searchcategory.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.searchcategory.presentation.model.ProductItemDataView

object VisitableMapper {
    fun MutableList<Visitable<*>>.updateWishlistStatus(productId: String, hasBeenWishlist: Boolean): Int? {
        val product = filterIsInstance<ProductItemDataView>().find { it.productCardModel.productId == productId }
        product?.apply {
            val index = indexOf(this)
            productCardModel = productCardModel.copy(hasBeenWishlist = hasBeenWishlist)
            return index
        }
        return null
    }

    fun MutableList<Visitable<*>>.updateProductItem(
        productId: String,
        hasBeenWishlist: Boolean
    ): Int {
        val productList = filterIsInstance<ProductItemDataView>()
        val product = productList.find { it.productCardModel.productId == productId }

        product?.let {
            val updatedProduct = it.copy(
                productCardModel = it.productCardModel.copy(
                    hasBeenWishlist = hasBeenWishlist
                )
            )
            val index = indexOf(it)
            this[index] = updatedProduct
        }

        return productList.indexOf(product)
    }
}
