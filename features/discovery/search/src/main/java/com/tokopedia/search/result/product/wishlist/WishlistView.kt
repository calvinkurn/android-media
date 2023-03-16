package com.tokopedia.search.result.product.wishlist

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.ProductCardOptionsModel.WishlistResult
import com.tokopedia.discovery.common.model.WishlistTrackingModel

interface WishlistView {

    val queryKey: String
    fun launchLoginActivity(productId: String?)
    fun trackWishlistRecommendationProductLoginUser(isAddWishlist: Boolean)
    fun trackWishlistRecommendationProductNonLoginUser()
    fun trackWishlistProduct(wishlistTrackingModel: WishlistTrackingModel)
    fun updateWishlistStatus(productId: String, isWishlisted: Boolean)
    fun hitWishlistClickUrl(productCardOptionsModel: ProductCardOptionsModel)
    fun showMessageSuccessWishlistAction(wishlistResult: WishlistResult)
    fun showMessageFailedWishlistAction(wishlistResult: WishlistResult)
}
