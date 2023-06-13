package com.tokopedia.search.result.product.wishlist

import com.tokopedia.discovery.common.model.ProductCardOptionsModel

interface WishlistPresenter {

    fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel?)
}
