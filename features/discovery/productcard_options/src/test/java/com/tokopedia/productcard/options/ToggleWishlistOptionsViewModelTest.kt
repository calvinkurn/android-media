package com.tokopedia.productcard.options

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.productcard.options.testutils.TestException
import com.tokopedia.productcard.options.testutils.complete
import com.tokopedia.productcard.options.testutils.error
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase.WISHSLIST_URL
import com.tokopedia.usecase.RequestParams
import com.tokopedia.wishlist.common.listener.WishListActionListener
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.slot
import org.junit.Test
import rx.Subscriber

internal class ToggleWishlistOptionsViewModelTest: ProductCardOptionsViewModelTestFixtures() {
    
    @Test
    fun `Save to Wishlist for non login user`() {
        val productCardOptionsModelNotWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = false, productId = "12345")

        `Given Product Card Options View Model`(productCardOptionsModelNotWishlisted)
        `Given User is not logged in`()

        `When Click save to wishlist`()

        `Then should post wishlist event`()
        `Then should post event close product card options`()
        `Then assert product card options model wishlist result isUserLoggedIn is false`()
    }

    private fun `Given Product Card Options View Model`(productCardOptionsModel: ProductCardOptionsModel) {
        createProductCardOptionsViewModel(productCardOptionsModel)
    }

    private fun `Given User is not logged in`() {
        every { userSession.isLoggedIn }.returns(false)
    }

    private fun `When Click save to wishlist`() {
        productCardOptionsViewModel.getOption(SAVE_TO_WISHLIST).onClick()
    }

    private fun `Then should post wishlist event`() {
        val wishlistEventLiveData = productCardOptionsViewModel.getWishlistEventLiveData().value

        wishlistEventLiveData?.getContentIfNotHandled().shouldBe(
                true,
                "Wishlist event should be true"
        )
    }

    private fun `Then should post event close product card options`() {
        val closeProductCardOptionsEvent = productCardOptionsViewModel.getCloseProductCardOptionsEventLiveData().value

        closeProductCardOptionsEvent?.getContentIfNotHandled() shouldBe true
    }

    private fun `Then assert product card options model wishlist result isUserLoggedIn is false`() {
        val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

        wishlistResult.isUserLoggedIn shouldBe false
    }

    @Test
    fun `Add Wishlist Non-TopAds Product Success`() {
        val userId = "123456"
        val productCardOptionsModelNotWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = false, productId = "12345")

        `Given Product Card Options View Model`(productCardOptionsModelNotWishlisted)
        `Given user is logged in`(userId)
        `Given add wishlist API will be successful`(userId)

        `When Click save to wishlist`()

        `Then should post wishlist event`()
        `Then assert product card options model has wishlist result with isUserLoggedIn = true, isAddWishlist = true, and isSuccess = true`()
    }

    private fun `Given user is logged in`(userId: String) {
        every { userSession.isLoggedIn }.returns(true)
        every { userSession.userId }.returns(userId)
    }

    private fun `Given add wishlist API will be successful`(userId: String) {
        val productId = productCardOptionsViewModel.productCardOptionsModel?.productId ?: "0"

        every {
            addWishListUseCase.createObservable(productId, userId, any())
        }.answers {
            thirdArg<WishListActionListener>().onSuccessAddWishlist(firstArg())
        }
    }

    private fun `Then assert product card options model has wishlist result with isUserLoggedIn = true, isAddWishlist = true, and isSuccess = true`() {
        val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

        wishlistResult.isUserLoggedIn.shouldBe(
                true,
                "Wishlist result isUserLoggedIn should be true"
        )

        wishlistResult.isSuccess.shouldBe(
                true,
                "Wishlist result isSuccess should be true"
        )

        wishlistResult.isAddWishlist.shouldBe(
                true,
                "Wishlist result isAddWishlist should be true"
        )
    }

    @Test
    fun `Add Wishlist Non-TopAds Product Failed and handled in onErrorAddWishlist`() {
        val userId = "123456"
        val productCardOptionsModelNotWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = false, productId = "12345")

        `Given Product Card Options View Model`(productCardOptionsModelNotWishlisted)
        `Given user is logged in`(userId)
        `Given add wishlist API will fail`(userId)

        `When Click save to wishlist`()

        `Then should post wishlist event`()
        `Then assert product card options model has wishlist result with isUserLoggin = true, isAddWishlist = true, and isSuccess = false`()
    }

    private fun `Given add wishlist API will fail`(userId: String) {
        val productId = productCardOptionsViewModel.productCardOptionsModel?.productId ?: "0"

        every {
            addWishListUseCase.createObservable(productId, userId, any())
        }.answers {
            thirdArg<WishListActionListener>().onErrorAddWishList("error from backend", firstArg())
        }
    }

    private fun `Then assert product card options model has wishlist result with isUserLoggin = true, isAddWishlist = true, and isSuccess = false`() {
        val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

        wishlistResult.isUserLoggedIn.shouldBe(
                true,
                "Wishlist result isUserLoggedIn should be true"
        )
        wishlistResult.isSuccess.shouldBe(
                false,
                "Wishlist result isSuccess should be false"
        )

        wishlistResult.isAddWishlist.shouldBe(
                true,
                "Wishlist result isAddWishlist should be true"
        )
    }

    @Test
    fun `Add Wishlist Non-TopAds Product Failed and throw Exception`() {
        val userId = "123456"
        val testException = TestException()
        val productCardOptionsModelNotWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = false, productId = "12345")

        `Given Product Card Options View Model`(productCardOptionsModelNotWishlisted)
        `Given user is logged in`(userId)
        `Given add wishlist API will fail with exception`(userId, testException)

        `When Click save to wishlist`()

        `Then assert error stack trace is printed`(testException)
        `Then should post wishlist event`()
        `Then assert product card options model has wishlist result with isUserLoggedIn = true, isAddWishlist = true and isSuccess = false`()
    }

    private fun `Given add wishlist API will fail with exception`(userId: String, testException: Exception) {
        val productId = productCardOptionsViewModel.productCardOptionsModel?.productId ?: "0"

        every {
            addWishListUseCase.createObservable(productId, userId, any())
        }.throws(testException)
    }

    private fun `Then assert error stack trace is printed`(testException: TestException) {
        testException.isStackTracePrinted.shouldBe(true,
                "Exception stack trace printed should be true")
    }

    private fun `Then assert product card options model has wishlist result with isUserLoggedIn = true, isAddWishlist = true and isSuccess = false`() {
        val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

        wishlistResult.isUserLoggedIn.shouldBe(
                true,
                "Wishlist result isUserLoggedIn should be true"
        )

        wishlistResult.isSuccess.shouldBe(
                false,
                "Wishlist result isSuccess should be false"
        )

        wishlistResult.isAddWishlist.shouldBe(
                true,
                "Wishlist result isAddWishlist should be true"
        )
    }

    @Test
    fun `Add Wishlist TopAds Product Success`() {
        val userId = "123456"
        val topAdsWishlistUrl = "https://dummy_topads_url_for_wishlist"
        val topAdsWishlistRequestParams = slot<RequestParams>()
        val productCardOptionsModelNotWishlisted = ProductCardOptionsModel(
                hasWishlist = true,
                isWishlisted = false,
                productId = "12345",
                isTopAds = true,
                topAdsWishlistUrl = topAdsWishlistUrl
        )

        `Given Product Card Options View Model`(productCardOptionsModelNotWishlisted)
        `Given user is logged in`(userId)
        `Given add TopAds wishlist API will be successful`(topAdsWishlistRequestParams)

        `When Click save to wishlist`()

        `Then verify TopAds wishlist API is called with correct parameters`(topAdsWishlistRequestParams, topAdsWishlistUrl)
        `Then should post wishlist event`()
        `Then assert product card options model has wishlist result with isUserLoggedIn = true, isAddWishlist = true, and isSuccess = true`()
    }

    private fun `Given add TopAds wishlist API will be successful`(topAdsWishlistRequestParams: CapturingSlot<RequestParams>) {
        every {
            topAdsWishlistUseCase.execute(capture(topAdsWishlistRequestParams), any())
        } answers {
            secondArg<Subscriber<Boolean>>().complete(true)
        }
    }

    private fun `Then verify TopAds wishlist API is called with correct parameters`(
            topAdsWishlistRequestParams: CapturingSlot<RequestParams>,
            topAdsWishlistUrl: String
    ) {
        val requestParams = topAdsWishlistRequestParams.captured
        val wishlistUrl = requestParams.getString(WISHSLIST_URL, "")

        wishlistUrl shouldBe topAdsWishlistUrl
    }

    @Test
    fun `Add Wishlist TopAds Product Failed and throw Exception`() {
        val userId = "123456"
        val topAdsWishlistUrl = "https://dummy_topads_url_for_wishlist"
        val testException = TestException()
        val topAdsWishlistRequestParams = slot<RequestParams>()
        val productCardOptionsModelNotWishlisted = ProductCardOptionsModel(
                hasWishlist = true,
                isWishlisted = false,
                productId = "12345",
                isTopAds = true,
                topAdsWishlistUrl = topAdsWishlistUrl
        )

        `Given Product Card Options View Model`(productCardOptionsModelNotWishlisted)
        `Given user is logged in`(userId)
        `Given add TopAds wishlist API will fail`(topAdsWishlistRequestParams, testException)

        `When Click save to wishlist`()

        `Then assert error stack trace is printed`(testException)
        `Then should post wishlist event`()
    }

    private fun `Given add TopAds wishlist API will fail`(topAdsWishlistRequestParams: CapturingSlot<RequestParams>, testException: Exception) {
        every {
            topAdsWishlistUseCase.execute(capture(topAdsWishlistRequestParams), any())
        } answers {
            secondArg<Subscriber<Boolean>>().error(testException)
        }
    }

    @Test
    fun `Delete from Wishlist for non login user`() {
        val productCardOptionsModelWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = true, productId = "12345")

        `Given Product Card Options View Model`(productCardOptionsModelWishlisted)
        `Given User is not logged in`()

        `When Click delete from wishlist`()

        `Then should post wishlist event`()
        `Then should post event close product card options`()
        `Then assert product card options model wishlist result isUserLoggedIn is false`()
    }

    private fun `When Click delete from wishlist`() {
        productCardOptionsViewModel.getOption(DELETE_FROM_WISHLIST).onClick()
    }

    @Test
    fun `Delete Product from Wishlist Success`() {
        val userId = "123456"
        val productCardOptionsModelWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = true, productId = "12345")

        `Given Product Card Options View Model`(productCardOptionsModelWishlisted)
        `Given user is logged in`(userId)
        `Given remove wishlist API will be successful`(userId)

        `When Click delete from wishlist`()

        `Then should post wishlist event`()
        `Then assert product card options model has wishlist result with isAddWishlist = false and isSuccess = true`()
    }

    private fun `Given remove wishlist API will be successful`(userId: String) {
        val productId = productCardOptionsViewModel.productCardOptionsModel?.productId ?: "0"

        every {
            removeWishListUseCase.createObservable(productId, userId, any())
        }.answers {
            thirdArg<WishListActionListener>().onSuccessRemoveWishlist(firstArg())
        }
    }

    private fun `Then assert product card options model has wishlist result with isAddWishlist = false and isSuccess = true`() {
        val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

        wishlistResult.isUserLoggedIn.shouldBe(
                true,
                "Wishlist result isUserLoggedIn should be true"
        )

        wishlistResult.isSuccess.shouldBe(
                true,
                "Wishlist result isSuccess should be true"
        )

        wishlistResult.isAddWishlist.shouldBe(
                false,
                "Wishlist result isAddWishlist should be false"
        )
    }

    @Test
    fun `Delete Wishlist Product Failed and handled in onErrorRemoveWishlist`() {
        val userId = "123456"
        val productCardOptionsModelWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = true, productId = "12345")

        `Given Product Card Options View Model`(productCardOptionsModelWishlisted)
        `Given user is logged in`(userId)
        `Given remove wishlist API will fail`(userId)

        `When Click delete from wishlist`()

        `Then should post wishlist event`()
        `Then assert product card options model has wishlist result with isAddWishlist = false and isSuccess = false`()
    }

    private fun `Given remove wishlist API will fail`(userId: String) {
        val productId = productCardOptionsViewModel.productCardOptionsModel?.productId ?: "0"

        every {
            removeWishListUseCase.createObservable(productId, userId, any())
        }.answers {
            thirdArg<WishListActionListener>().onErrorRemoveWishlist("error from backend", firstArg())
        }
    }

    private fun `Then assert product card options model has wishlist result with isAddWishlist = false and isSuccess = false`() {
        val wishlistResult = productCardOptionsViewModel.productCardOptionsModel?.wishlistResult!!

        wishlistResult.isUserLoggedIn.shouldBe(
                true,
                "Wishlist result isUserLoggedIn should be true"
        )

        wishlistResult.isSuccess.shouldBe(
                false,
                "Wishlist result isSuccess should be false"
        )

        wishlistResult.isAddWishlist.shouldBe(
                false,
                "Wishlist result isAddWishlist should be false"
        )
    }

    @Test
    fun `Delete Wishlist Product Failed`() {
        val userId = "123456"
        val testException = TestException()
        val productCardOptionsModelWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = true, productId = "12345")

        `Given Product Card Options View Model`(productCardOptionsModelWishlisted)
        `Given user is logged in`(userId)
        `Given remove wishlist API will fail`(userId, testException)

        `When Click delete from wishlist`()

        `Then assert error stack trace is printed`(testException)
        `Then should post wishlist event`()
        `Then assert product card options model has wishlist result with isAddWishlist = false and isSuccess = false`()
    }

    private fun `Given remove wishlist API will fail`(userId: String, testException: Exception) {
        val productId = productCardOptionsViewModel.productCardOptionsModel?.productId ?: "0"

        every {
            removeWishListUseCase.createObservable(productId, userId, any())
        }.throws(testException)
    }
}