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
}
