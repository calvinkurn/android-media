package com.tokopedia.similarsearch.viewmodel

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.similarsearch.testutils.shouldBe
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelCommon
import org.junit.Test

internal class HandleReceiveProductCardOptionsWishlistTest: SimilarSearchTestFixtures() {

    @Test
    fun `Receive wishlist result user not logged in`() {
        val productCardOptionsModel = ProductCardOptionsModel().also {
            it.productId = "12345"
            it.isWishlisted = false
            it.wishlistResult = ProductCardOptionsModel.WishlistResult(
                isUserLoggedIn = false
            )
        }

        `When receive product card options wishlist result`(productCardOptionsModel)

        `Then verify route to login page is true`()
        `Then verify tracking wishlist event has correct WishlistTrackingModel`(
            WishlistTrackingModel(
                isAddWishlist = !productCardOptionsModel.isWishlisted,
                productId = productCardOptionsModel.productId,
                isTopAds = productCardOptionsModel.isTopAds,
                keyword = getSimilarSearchQuery(),
                isUserLoggedIn = productCardOptionsModel.wishlistResult.isUserLoggedIn
            )
        )
        `Then verify wishlist event is null`()
    }

    private fun `When receive product card options wishlist result`(
        productCardOptionsModel: ProductCardOptionsModel
    ) {
        similarSearchViewModel.onReceiveProductCardOptionsWishlistResult(productCardOptionsModel)
    }

    private fun `Then verify route to login page is true`() {
        val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value

        routeToLoginEvent?.getContentIfNotHandled().shouldBe(
            true,
            "Route to login page event should be true"
        )
    }

    private fun `Then verify tracking wishlist event has correct WishlistTrackingModel`(
        expectedWishlistTrackingModel: WishlistTrackingModel
    ) {
        val trackingWishlistEventLiveData =
            similarSearchViewModel.getTrackingWishlistEventLiveData().value

        trackingWishlistEventLiveData?.getContentIfNotHandled() shouldBe expectedWishlistTrackingModel
    }

    private fun `Then verify wishlist event is null`() {
        val addWishlistEvent = similarSearchViewModel.getAddWishlistEventLiveData().value
        val removeWishlistEvent = similarSearchViewModel.getRemoveWishlistEventLiveData().value


        addWishlistEvent?.getContentIfNotHandled()
            .shouldBe(null, "Add wishlist event should be null")
        removeWishlistEvent?.getContentIfNotHandled()
            .shouldBe(null, "Remove wishlist event should be null")
    }

    @Test
    fun `Receive wishlist result add to wishlist success`() {
        val similarProductModel = getSimilarProductModelCommon()
        val productId = similarProductModel.getSimilarProductList()[0].id
        val productCardOptionsModel = ProductCardOptionsModel().also {
            it.productId = productId
            it.isWishlisted = false
            it.wishlistResult = ProductCardOptionsModel.WishlistResult(
                isUserLoggedIn = true,
                isSuccess = true,
                isAddWishlist = true
            )
        }

        `Given view already created and has similar search data`(similarProductModel)

        `When receive product card options wishlist result`(productCardOptionsModel)

        `Then verify add wishlist event is true`()
        `Then verify similar search view model list is updated with chosen similar product isWishlisted is true`(
            productId
        )
        `Then verify tracking wishlist event has correct WishlistTrackingModel`(
            WishlistTrackingModel(
                isAddWishlist = !productCardOptionsModel.isWishlisted,
                productId = productCardOptionsModel.productId,
                isTopAds = productCardOptionsModel.isTopAds,
                keyword = getSimilarSearchQuery(),
                isUserLoggedIn = productCardOptionsModel.wishlistResult.isUserLoggedIn
            )
        )
        `Then verify remove wishlist event is null`()
        `Then Verify route to login page event should be null`()
    }

    private fun `Then verify add wishlist event is true`() {
        val addWishlistEvent = similarSearchViewModel.getAddWishlistEventLiveData().value
        addWishlistEvent?.getContentIfNotHandled()
            .shouldBe(true, "Add wishlist event should be true")
    }

    private fun `Then verify similar search view model list is updated with chosen similar product isWishlisted is true`(
        productId: String
    ) {
        val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
        val similarSearchViewModelList = similarSearchLiveData?.data ?: listOf()
        similarSearchViewModelList.shouldHaveSimilarProductWithExpectedWishlistStatus(
            productId,
            true
        )
    }

    private fun `Then verify remove wishlist event is null`() {
        val removeWishlistEvent = similarSearchViewModel.getRemoveWishlistEventLiveData().value
        removeWishlistEvent?.getContentIfNotHandled()
            .shouldBe(null, "Remove wishlist event should be null")
    }

    private fun `Then Verify route to login page event should be null`() {
        val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value

        routeToLoginEvent?.getContentIfNotHandled()
            .shouldBe(null, "Route to login page event should be null")
    }

    @Test
    fun `Receive wishlist result add to wishlist failed`() {
        val similarProductModel = getSimilarProductModelCommon()
        val productId = similarProductModel.getSimilarProductList()[0].id
        val productCardOptionsModel = ProductCardOptionsModel().also {
            it.productId = productId
            it.isWishlisted = false
            it.wishlistResult = ProductCardOptionsModel.WishlistResult(
                isUserLoggedIn = true,
                isSuccess = false,
                isAddWishlist = true
            )
        }

        `Given view already created and has similar search data`(similarProductModel)

        `When receive product card options wishlist result`(productCardOptionsModel)

        `Then Verify Add wishlist event should be false`()
        `Then Verify tracking wishlist event is null`()
        `Then Verify remove wishlist event should be null`()
        `Then Verify route to login page event should be null`()
    }

    private fun `Then Verify Add wishlist event should be false`() {
        val addWishlistEvent = similarSearchViewModel.getAddWishlistEventLiveData().value
        addWishlistEvent?.getContentIfNotHandled().shouldBe(false, "Add wishlist event should be false")
    }

    private fun `Then Verify remove wishlist event should be null`() {
        val removeWishlistEvent = similarSearchViewModel.getRemoveWishlistEventLiveData().value
        removeWishlistEvent?.getContentIfNotHandled().shouldBe(null, "Remove wishlist event should be null")
    }

    @Test
    fun `Receive wishlist result remove wishlist success`() {
        val similarProductModel = getSimilarProductModelCommon()
        val productId = similarProductModel.getSimilarProductList()[1].id
        val productCardOptionsModel = ProductCardOptionsModel().also {
            it.productId = productId
            it.isWishlisted = true
            it.wishlistResult = ProductCardOptionsModel.WishlistResult(
                isUserLoggedIn = true,
                isSuccess = true,
                isAddWishlist = false
            )
        }

        `Given view already created and has similar search data`(similarProductModel)

        `When receive product card options wishlist result`(productCardOptionsModel)

        `Then Verify remove wishlist event should be true`()
        `Then verify similar search view model list is updated with chosen similar product isWishlisted is false`(
            productId
        )
        `Then verify tracking wishlist event has correct WishlistTrackingModel`(
            WishlistTrackingModel(
                isAddWishlist = !productCardOptionsModel.isWishlisted,
                productId = productCardOptionsModel.productId,
                isTopAds = productCardOptionsModel.isTopAds,
                keyword = getSimilarSearchQuery(),
                isUserLoggedIn = productCardOptionsModel.wishlistResult.isUserLoggedIn
            )
        )
        `Then Verify Add wishlist event should be null`()
        `Then Verify route to login page event should be null`()
    }

    private fun `Then Verify remove wishlist event should be true`() {
        val removeWishlistEvent = similarSearchViewModel.getRemoveWishlistEventLiveData().value
        removeWishlistEvent?.getContentIfNotHandled().shouldBe(
            true,
            "Remove wishlist event should be true"
        )
    }

    private fun `Then verify similar search view model list is updated with chosen similar product isWishlisted is false`(
        productId: String
    ) {
        val similarSearchLiveData = similarSearchViewModel.getSimilarSearchLiveData().value
        val similarSearchViewModelList = similarSearchLiveData?.data ?: listOf()
        similarSearchViewModelList.shouldHaveSimilarProductWithExpectedWishlistStatus(
            productId,
            false
        )
    }

    private fun `Then Verify Add wishlist event should be null`() {
        val addWishlistEvent = similarSearchViewModel.getAddWishlistEventLiveData().value
        addWishlistEvent?.getContentIfNotHandled().shouldBe(
            null,
            "Add wishlist event should be null"
        )
    }

    @Test
    fun `Receive wishlist result remove from wishlist failed`() {
        val similarProductModel = getSimilarProductModelCommon()
        val productId = similarProductModel.getSimilarProductList()[1].id
        val productCardOptionsModel = ProductCardOptionsModel().also {
            it.productId = productId
            it.isWishlisted = true
            it.wishlistResult = ProductCardOptionsModel.WishlistResult(
                isUserLoggedIn = true,
                isSuccess = false,
                isAddWishlist = false
            )
        }

        `Given view already created and has similar search data`(similarProductModel)

        `When receive product card options wishlist result`(productCardOptionsModel)

        `Then Verify Remove wishlist event should be false`()
        `Then Verify tracking wishlist event is null`()
        `Then Verify add wishlist event should be null`()
        `Then Verify route to login page event should be null`()
    }

    private fun `Then Verify Remove wishlist event should be false`() {
        val removeWishlistEvent = similarSearchViewModel.getRemoveWishlistEventLiveData().value
        removeWishlistEvent?.getContentIfNotHandled().shouldBe(false, "Remove wishlist event should be false")
    }

    private fun `Then Verify tracking wishlist event is null`() {
        val trackingWishlistEventLiveData = similarSearchViewModel.getTrackingWishlistEventLiveData().value
        trackingWishlistEventLiveData?.getContentIfNotHandled().shouldBe(null, "Tracking wishlist event should be null")
    }

    private fun `Then Verify add wishlist event should be null`() {
        val addWishlistEvent = similarSearchViewModel.getAddWishlistEventLiveData().value
        addWishlistEvent?.getContentIfNotHandled().shouldBe(null, "Add wishlist event should be null")
    }
}