package com.tokopedia.search.result.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.search.result.presentation.view.typefactory.ProductListTypeFactory
import com.tokopedia.search.result.product.wishlist.Wishlistable

class RecommendationItemDataView (
    val recommendationItem: RecommendationItem
): Visitable<ProductListTypeFactory>, ImpressHolder(), Wishlistable {
    override fun type(typeFactory: ProductListTypeFactory): Int {
        return typeFactory.type(this)
    }

    override val isWishlisted: Boolean
        get() = recommendationItem.isWishlist

    override fun setWishlist(productID: String, isWishlisted: Boolean) {
        if (recommendationItem.productId.toString() == productID) {
            recommendationItem.isWishlist = isWishlisted
        }
    }
}
