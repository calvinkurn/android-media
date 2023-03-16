package com.tokopedia.search.result.presentation.presenter.product

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import io.mockk.*
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
            wishlistView.trackWishlistRecommendationProductNonLoginUser()
            wishlistView.launchLoginActivity(productCardOptionsModel.productId)
        }

        confirmVerified(wishlistView)
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
            wishlistView.queryKey
            wishlistView.trackWishlistProduct(capture(slotWishlistTrackingModel))
            wishlistView.launchLoginActivity(productCardOptionsModel.productId)
        }

        confirmVerified(wishlistView)
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

    @Test
    fun `Handle success remove wishlist action for recommendation product`() {
        val productCardOptionsModel = ProductCardOptionsModel(
            productId = "12345",
            isTopAds = false,
            isWishlisted = true,
            isRecommendation = true
        ).also {
            it.wishlistResult = ProductCardOptionsModel.WishlistResult(isUserLoggedIn = true, isSuccess = true, isAddWishlist = true)
        }

        `When handle wishlist action`(productCardOptionsModel)

        `Then verify view interaction when wishlist recommendation product`(productCardOptionsModel)
    }

    @Test
    fun `Handle success wishlist action for recommendation - topads product`() {
        val productCardOptionsModel = ProductCardOptionsModel(
            productId = "12345",
            isTopAds = true,
            isWishlisted = false,
            isRecommendation = true
        ).also {
            it.wishlistResult = ProductCardOptionsModel.WishlistResult(isUserLoggedIn = true, isSuccess = true, isAddWishlist = true)
        }

        `When handle wishlist action`(productCardOptionsModel)

        `Then verify view interaction when wishlist recommendation - topads product`(productCardOptionsModel)
    }

    private fun `Then verify view interaction when wishlist recommendation product`(productCardOptionsModel: ProductCardOptionsModel) {
        verifyOrder {
            wishlistView.trackWishlistRecommendationProductLoginUser(!productCardOptionsModel.isWishlisted)
            wishlistView.updateWishlistStatus(productCardOptionsModel.productId, true)
            wishlistView.showMessageSuccessWishlistAction(productCardOptionsModel.wishlistResult)
        }

        confirmVerified(wishlistView)
    }

    private fun `Then verify view interaction when wishlist recommendation - topads product`(productCardOptionsModel: ProductCardOptionsModel) {
        verifySequence {
            wishlistView.trackWishlistRecommendationProductLoginUser(true)
            wishlistView.updateWishlistStatus(productCardOptionsModel.productId, true)
            wishlistView.showMessageSuccessWishlistAction(productCardOptionsModel.wishlistResult)
            wishlistView.hitWishlistClickUrl(productCardOptionsModel)
        }

        confirmVerified(wishlistView)
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

    @Test
    fun `Handle success wishlist action for non-recommendation topads product`() {
        val productCardOptionsModel = ProductCardOptionsModel(
            productId = "12345",
            isTopAds = true,
            isWishlisted = false,
            isRecommendation = false
        ).also {
            it.wishlistResult = ProductCardOptionsModel.WishlistResult(isUserLoggedIn = true, isSuccess = true, isAddWishlist = true)
        }

        `Given keyword from view`()

        `When handle wishlist action`(productCardOptionsModel)

        `Then verify view interaction for non-recommendation topads product`(productCardOptionsModel)

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
            wishlistView.queryKey
            wishlistView.trackWishlistProduct(capture(slotWishlistTrackingModel))
            wishlistView.updateWishlistStatus(productCardOptionsModel.productId, true)
            wishlistView.showMessageSuccessWishlistAction(productCardOptionsModel.wishlistResult)
        }

        confirmVerified(wishlistView)
    }

    private fun `Then verify view interaction for non-recommendation topads product`(productCardOptionsModel: ProductCardOptionsModel) {
        verifyOrder {
            wishlistView.queryKey
            wishlistView.trackWishlistProduct(capture(slotWishlistTrackingModel))
            wishlistView.updateWishlistStatus(productCardOptionsModel.productId, true)
            wishlistView.showMessageSuccessWishlistAction(productCardOptionsModel.wishlistResult)
            wishlistView.hitWishlistClickUrl(productCardOptionsModel)
        }

        confirmVerified(wishlistView)
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
            wishlistView.showMessageFailedWishlistAction(productCardOptionsModel.wishlistResult)
        }

        confirmVerified(wishlistView)
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
            wishlistView.showMessageFailedWishlistAction(productCardOptionsModel.wishlistResult)
        }

        confirmVerified(wishlistView)
    }
}
