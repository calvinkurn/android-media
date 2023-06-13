package com.tokopedia.search.result.product.wishlist

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.search.di.scope.SearchScope
import javax.inject.Inject

@SearchScope
class WishlistPresenterDelegate @Inject constructor(
    private val view: WishlistView,
): WishlistPresenter {

    override fun handleWishlistAction(productCardOptionsModel: ProductCardOptionsModel?) {
        productCardOptionsModel ?: return

        if (productCardOptionsModel.isRecommendation)
            handleWishlistRecommendationProduct(productCardOptionsModel)
        else
            handleWishlistNonRecommendationProduct(productCardOptionsModel)
    }

    private fun handleWishlistRecommendationProduct(productCardOptionsModel: ProductCardOptionsModel) {
        val wishlistResult = productCardOptionsModel.wishlistResult

        if (wishlistResult.isUserLoggedIn)
            handleWishlistRecommendationProductWithLoggedInUser(productCardOptionsModel)
        else
            handleWishlistRecommendationProductWithNotLoggedInUser(productCardOptionsModel)
    }

    private fun handleWishlistRecommendationProductWithLoggedInUser(
        productCardOptionsModel: ProductCardOptionsModel,
    ) {
        val wishlistResult = productCardOptionsModel.wishlistResult

        if (!wishlistResult.isSuccess) {
            view.showMessageFailedWishlistAction(wishlistResult)
        } else {
            view.trackWishlistRecommendationProductLoginUser(!productCardOptionsModel.isWishlisted)
            view.updateWishlistStatus(productCardOptionsModel.productId, wishlistResult.isAddWishlist)
            view.showMessageSuccessWishlistAction(wishlistResult)
            if (productCardOptionsModel.isTopAds) view.hitWishlistClickUrl(productCardOptionsModel)
        }
    }

    private fun handleWishlistRecommendationProductWithNotLoggedInUser(
        productCardOptionsModel: ProductCardOptionsModel,
    ) {
        view.trackWishlistRecommendationProductNonLoginUser()
        view.launchLoginActivity(productCardOptionsModel.productId)
    }

    private fun handleWishlistNonRecommendationProduct(
        productCardOptionsModel: ProductCardOptionsModel,
    ) {
        val wishlistResult = productCardOptionsModel.wishlistResult

        if (wishlistResult.isUserLoggedIn)
            handleWishlistNonRecommendationProductWithLoggedInUser(productCardOptionsModel)
        else
            handleWishlistNonRecommendationProductWithNotLoggedInUser(productCardOptionsModel)
    }

    private fun handleWishlistNonRecommendationProductWithLoggedInUser(
        productCardOptionsModel: ProductCardOptionsModel,
    ) {
        val wishlistResult = productCardOptionsModel.wishlistResult

        if (!wishlistResult.isSuccess) {
            view.showMessageFailedWishlistAction(wishlistResult)
        } else {
            view.trackWishlistProduct(createWishlistTrackingModel(
                productCardOptionsModel,
                productCardOptionsModel.wishlistResult.isAddWishlist
            ))
            view.updateWishlistStatus(productCardOptionsModel.productId, wishlistResult.isAddWishlist)
            view.showMessageSuccessWishlistAction(wishlistResult)
            if (productCardOptionsModel.isTopAds) view.hitWishlistClickUrl(productCardOptionsModel)
        }
    }

    private fun createWishlistTrackingModel(
        productCardOptionsModel: ProductCardOptionsModel,
        isAddWishlist: Boolean,
    ): WishlistTrackingModel {
        val wishlistTrackingModel = WishlistTrackingModel()

        wishlistTrackingModel.productId = productCardOptionsModel.productId
        wishlistTrackingModel.isTopAds = productCardOptionsModel.isTopAds
        wishlistTrackingModel.keyword = view.queryKey
        wishlistTrackingModel.isUserLoggedIn = productCardOptionsModel.wishlistResult.isUserLoggedIn
        wishlistTrackingModel.isAddWishlist = isAddWishlist

        return wishlistTrackingModel
    }

    private fun handleWishlistNonRecommendationProductWithNotLoggedInUser(
        productCardOptionsModel: ProductCardOptionsModel,
    ) {
        view.trackWishlistProduct(createWishlistTrackingModel(
            productCardOptionsModel,
            !productCardOptionsModel.isWishlisted
        ))
        view.launchLoginActivity(productCardOptionsModel.productId)
    }
}
