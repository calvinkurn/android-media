package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.slot
import io.mockk.verifyOrder
import org.junit.Test

internal class SearchProductHandleWishlistActionTest: ProductListPresenterTestFixtures() {

    private val keyword = "samsung"
    private val slotWishlistTrackingModel = slot<WishlistTrackingModel>()

    @Test
    fun `Handle wishlist action with null product card options model (degenerate cases)`() {
        `When handle wishlist action`(null)

        `Then verify view does not do anything`()
    }

    private fun `When handle wishlist action`(productCardOptionsModel: ProductCardOptionsModel?) {
        productListPresenter.handleWishlistAction(productCardOptionsModel)
    }

    private fun `Then verify view does not do anything`() {
        confirmVerified(productListView)
    }

    @Test
    fun `Handle wishlist action for recommendation product with non-login user`() {
        val productCardOptionsModel = ProductCardOptionsModel(
                productId = "12345",
                isRecommendation = true
        ).also {
            it.wishlistResult = ProductCardOptionsModel.WishlistResult(isUserLoggedIn = false)
        }

        `When handle wishlist action`(productCardOptionsModel)

        `Then verify view interaction when wishlist recommendation product with non-login user`(productCardOptionsModel)
    }

    private fun `Then verify view interaction when wishlist recommendation product with non-login user`(productCardOptionsModel: ProductCardOptionsModel) {
        verifyOrder {
            productListView.trackWishlistRecommendationProductNonLoginUser()
            productListView.launchLoginActivity(productCardOptionsModel.productId)
        }

        confirmVerified(productListView)
    }

    @Test
    fun `Handle wishlist action for non-recommendation product with non-login user`() {
        val productCardOptionsModel = ProductCardOptionsModel(
                productId = "12345",
                isTopAds = false,
                isWishlisted = false,
                isRecommendation = false
        ).also {
            it.wishlistResult = ProductCardOptionsModel.WishlistResult(isUserLoggedIn = false)
        }

        `Given keyword from view`()

        `When handle wishlist action`(productCardOptionsModel)

        `Then verify view interaction when wishlist non-recommendation product with non-login user`(productCardOptionsModel)

        val expectedWishlistTrackingModel = WishlistTrackingModel(
                isAddWishlist = !productCardOptionsModel.isWishlisted,
                productId = productCardOptionsModel.productId,
                isTopAds = productCardOptionsModel.isTopAds,
                isUserLoggedIn = productCardOptionsModel.wishlistResult.isUserLoggedIn,
                keyword = keyword
        )

        `Then verify wishlist tracking model is correct`(expectedWishlistTrackingModel)
    }

    private fun `Given keyword from view`() {
        every { productListView.queryKey } returns keyword
    }

    private fun `Then verify view interaction when wishlist non-recommendation product with non-login user`(productCardOptionsModel: ProductCardOptionsModel) {
        verifyOrder {
            productListView.queryKey
            productListView.trackWishlistProduct(capture(slotWishlistTrackingModel))
            productListView.launchLoginActivity(productCardOptionsModel.productId)
        }

        confirmVerified(productListView)
    }

    private fun `Then verify wishlist tracking model is correct`(expectedWishlistTrackingModel: WishlistTrackingModel) {
        val wishlistTrackingModel = slotWishlistTrackingModel.captured

        wishlistTrackingModel.assert(expectedWishlistTrackingModel)
    }

    @Test
    fun `Handle success wishlist action for recommendation product`() {
        val productCardOptionsModel = ProductCardOptionsModel(
                productId = "12345",
                isTopAds = false,
                isWishlisted = false,
                isRecommendation = true
        ).also {
            it.wishlistResult = ProductCardOptionsModel.WishlistResult(isUserLoggedIn = true, isSuccess = true, isAddWishlist = true)
        }

        `When handle wishlist action`(productCardOptionsModel)

        `Then verify view interaction when wishlist recommendation product`(productCardOptionsModel)
    }

    private fun `Then verify view interaction when wishlist recommendation product`(productCardOptionsModel: ProductCardOptionsModel) {
        verifyOrder {
            productListView.trackWishlistRecommendationProductLoginUser(true)
            productListView.updateWishlistStatus(productCardOptionsModel.productId, true)
            productListView.showMessageSuccessWishlistAction(productCardOptionsModel.wishlistResult.isAddWishlist)
        }

        confirmVerified(productListView)
    }

    @Test
    fun `Handle success wishlist action for non-recommendation product`() {
        val productCardOptionsModel = ProductCardOptionsModel(
                productId = "12345",
                isTopAds = false,
                isWishlisted = false,
                isRecommendation = false
        ).also {
            it.wishlistResult = ProductCardOptionsModel.WishlistResult(isUserLoggedIn = true, isSuccess = true, isAddWishlist = true)
        }

        `Given keyword from view`()

        `When handle wishlist action`(productCardOptionsModel)

        `Then verify view interaction for non-recommendation product`(productCardOptionsModel)

        val expectedWishlistTrackingModel = WishlistTrackingModel(
                isAddWishlist = productCardOptionsModel.wishlistResult.isAddWishlist,
                productId = productCardOptionsModel.productId,
                isTopAds = productCardOptionsModel.isTopAds,
                isUserLoggedIn = productCardOptionsModel.wishlistResult.isUserLoggedIn,
                keyword = keyword
        )

        `Then verify wishlist tracking model is correct`(expectedWishlistTrackingModel)
    }

    private fun `Then verify view interaction for non-recommendation product`(productCardOptionsModel: ProductCardOptionsModel) {
        verifyOrder {
            productListView.queryKey
            productListView.trackWishlistProduct(capture(slotWishlistTrackingModel))
            productListView.updateWishlistStatus(productCardOptionsModel.productId, true)
            productListView.showMessageSuccessWishlistAction(productCardOptionsModel.wishlistResult.isAddWishlist)
        }

        confirmVerified(productListView)
    }

    @Test
    fun `Handle failed wishlist action for recommendation product`() {
        val productCardOptionsModel = ProductCardOptionsModel(
                productId = "12345",
                isTopAds = false,
                isWishlisted = false,
                isRecommendation = true
        ).also {
            it.wishlistResult = ProductCardOptionsModel.WishlistResult(isUserLoggedIn = true, isSuccess = false, isAddWishlist = true)
        }

        `When handle wishlist action`(productCardOptionsModel)

        `Then verify view interaction when failed wishlist recommendation product`(productCardOptionsModel)
    }

    private fun `Then verify view interaction when failed wishlist recommendation product`(productCardOptionsModel: ProductCardOptionsModel) {
        verifyOrder {
            productListView.showMessageFailedWishlistAction(productCardOptionsModel.wishlistResult.isAddWishlist)
        }

        confirmVerified(productListView)
    }

    @Test
    fun `Handle failed wishlist action for non-recommendation product`() {
        val productCardOptionsModel = ProductCardOptionsModel(
                productId = "12345",
                isTopAds = false,
                isWishlisted = false,
                isRecommendation = false
        ).also {
            it.wishlistResult = ProductCardOptionsModel.WishlistResult(isUserLoggedIn = true, isSuccess = false, isAddWishlist = true)
        }

        `When handle wishlist action`(productCardOptionsModel)

        `Then verify view interaction when failed wishlist non-recommendation product`(productCardOptionsModel)
    }

    private fun `Then verify view interaction when failed wishlist non-recommendation product`(productCardOptionsModel: ProductCardOptionsModel) {
        verifyOrder {
            productListView.showMessageFailedWishlistAction(productCardOptionsModel.wishlistResult.isAddWishlist)
        }

        confirmVerified(productListView)
    }
}