package com.tokopedia.productcard.options

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.productcard.options.testutils.TestException
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase.WISHSLIST_URL
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import io.mockk.*
import org.junit.Test

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
    fun `Add WishlistV2 Non-TopAds Product Success`() {
        val userId = "123456"
        val productCardOptionsModelNotWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = false, productId = "12345")

        `Given Product Card Options View Model`(productCardOptionsModelNotWishlisted)
        `Given user is logged in`(userId)
        `Given add wishlistV2 GQL will be successful`()

        `When Click save to wishlist`()

        `Then should post wishlist event`()
        `Then assert product card options model has wishlist result with isUserLoggedIn = true, isAddWishlist = true, and isSuccess = true`()
    }

    private fun `Given user is logged in`(userId: String) {
        every { userSession.isLoggedIn }.returns(true)
        every { userSession.userId }.returns(userId)
    }

    private fun `Given add wishlistV2 GQL will be successful`() {
        val resultWishlistAddV2 = AddToWishlistV2Response.Data.WishlistAddV2(success = true)

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistAddV2)
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
    fun `Add WishlistV2 Non-TopAds Product Failed and handled in onErrorAddWishlist`() {
        val userId = "123456"
        val productCardOptionsModelNotWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = false, productId = "12345")

        `Given Product Card Options View Model`(productCardOptionsModelNotWishlisted)
        `Given user is logged in`(userId)
        `Given add wishlistV2 GQL will fail`()

        `When Click save to wishlist`()

        `Then should post wishlist event`()
        `Then assert product card options model has wishlist result with isUserLoggin = true, isAddWishlist = true, and isSuccess = false`()
    }

    @Test
    fun `Remove WishlistV2 Non-TopAds Product Failed and handled in onErrorRemoveWishlist`() {
        val userId = "123456"
        val productCardOptionsModelNotWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = true, productId = "12345")

        `Given Product Card Options View Model`(productCardOptionsModelNotWishlisted)
        `Given user is logged in`(userId)
        `Given remove wishlistV2 GQL will fail`()

        `When Click delete from wishlist`()

        `Then should post wishlist event`()
        `Then assert product card options model has wishlist result with isUserLoggin = true, isAddWishlist = false, and isSuccess = false`()
    }

    private fun `Given add wishlistV2 GQL will fail`() {
        val mockThrowable = mockk<Throwable>("fail")

        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)
    }

    private fun `Given remove wishlistV2 GQL will fail`() {
        val mockThrowable = mockk<Throwable>("fail")

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)
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

    private fun `Then assert product card options model has wishlist result with isUserLoggin = true, isAddWishlist = false, and isSuccess = false`() {
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

    private fun `Then verify TopAds wishlist API is called with correct parameters`(
            topAdsWishlistRequestParams: CapturingSlot<RequestParams>,
            topAdsWishlistUrl: String
    ) {
        val requestParams = topAdsWishlistRequestParams.captured
        val wishlistUrl = requestParams.getString(WISHSLIST_URL, "")

        wishlistUrl shouldBe topAdsWishlistUrl
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
    fun `Delete Product from WishlistV2 Success`() {
        val userId = "123456"
        val productCardOptionsModelWishlisted = ProductCardOptionsModel(hasWishlist = true, isWishlisted = true, productId = "12345")

        `Given Product Card Options View Model`(productCardOptionsModelWishlisted)
        `Given user is logged in`(userId)
        `Given remove wishlistV2 API will be successful`()

        `When Click delete from wishlist`()

        `Then should post wishlist event`()
        `Then assert product card options model has wishlist result with isAddWishlist = false and isSuccess = true`()
    }

    private fun `Given remove wishlistV2 API will be successful`() {
        val resultRemoveWishlistV2 = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Success(resultRemoveWishlistV2)
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
}
