package com.tokopedia.similarsearch.viewmodel

import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import com.tokopedia.similarsearch.testutils.shouldBe
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelCommon
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelOriginalProductWishlisted
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import io.mockk.*
import org.junit.Test

internal class HandleViewToggleWishlistOriginalProductTest: SimilarSearchTestFixtures() {
    
    @Test
    fun `Handle View Toggle WishlistV2 Original Product for non-login user`() {
        val similarProductModelCommon = getSimilarProductModelCommon()

        `Given user is not logged in`()
        `Given view already created and has similar search data`(similarProductModelCommon)

        `When handle view toggle wishlistV2 Original product`()

        `Then should post event go to login page`()
    }

    private fun `When handle view toggle wishlistV2 Original product`() {
        similarSearchViewModel.onViewToggleWishlistV2OriginalProduct()
    }

    private fun `Then should post event go to login page`() {
        val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value

        routeToLoginEvent?.getContentIfNotHandled().shouldBe(true,
            "Route to login page should be true")
    }

    @Test
    fun `Handle View Toggle WishlistV2 Original Product for logged in user and product is not wishlisted`() {
        val userId = "123456"
        val similarProductModelCommon = getSimilarProductModelCommon()
        val originalProduct = similarProductModelCommon.getOriginalProduct()

        `Given view already created and has similar search data`(similarProductModelCommon)
        `Given user is logged in with user id`(userId)

        `When handle view toggle wishlistV2 Original product`()

        `Then verify add wishlistV2 API is called with product id equals to Original product id`(originalProduct, userId)
    }

    private fun `Then verify add wishlistV2 API is called with product id equals to Original product id`(
        originalProduct: Product,
        userId: String
    ) {
        verify { addToWishlistV2UseCase.setParams(originalProduct.id, userId) }
        coVerify { addToWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `Handle View Toggle WishlistV2 Original Product for logged in user and product is wishlisted`() {
        val userId = "123456"
        val similarProductModelOriginalProductWishlisted = getSimilarProductModelOriginalProductWishlisted()
        val originalProduct = similarProductModelOriginalProductWishlisted.getOriginalProduct()

        `Given user is logged in with user id`(userId)
        `Given view already created and has similar search data`(similarProductModelOriginalProductWishlisted)

        `When handle view toggle wishlistV2 Original product`()

        `Then verify remove wishlistV2 API is called with product id equals to Original product id`(originalProduct, userId)
    }

    private fun `Then verify remove wishlistV2 API is called with product id equals to Original product id`(
        originalProduct: Product,
        userId: String
    ) {
        verify { deleteWishlistV2UseCase.setParams(originalProduct.id, userId) }
        coVerify { deleteWishlistV2UseCase.executeOnBackground() }
    }

    @Test
    fun `Add WishlistV2 Original Product Success`() {
        val userId = "123456"
        val similarProductModelCommon = getSimilarProductModelCommon()
        val originalProduct = similarProductModelCommon.getOriginalProduct()

        `Given user is logged in with user id`(userId)

        `Given view already created and has similar search data`(similarProductModelCommon)
        `Given add wishlistV2 API will be successful`()

        `When handle view toggle wishlistV2 Original product`()

        `Then verify add wishlistV2 API is called with product id equals to Original product id`(originalProduct, userId)
        `Then assert add wishlistV2 success field is true`()
        `Then assert Original product is wishlisted is true, and update wishlist Original product event is true`()
    }

    private fun `Given add wishlistV2 API will be successful`() {
        val resultWishlistAddV2 = AddToWishlistV2Response.Data.WishlistAddV2(success = true)
        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistAddV2)
    }

    private fun `Then assert add wishlist event is true`() {
        val addWishlistEventLiveData = similarSearchViewModel.getAddWishlistEventLiveData().value

        addWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
            true,
            "Add wishlist event should be true"
        )
    }

    private fun `Then assert add wishlistV2 success field is true`() {
        val addWishlistEventLiveData = similarSearchViewModel.getAddWishlistV2EventLiveData().value

        addWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
            AddToWishlistV2Response.Data.WishlistAddV2(success = true),
            "Add wishlistV2 field should return success"
        )
    }

    private fun `Then assert remove wishlistV2 success field is true`() {
        val removeWishlistEventLiveData = similarSearchViewModel.getRemoveWishlistV2EventLiveData().value

        removeWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
            DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true),
            "Remove wishlistV2 field should return success"
        )
    }

    private fun `Then assert Original product is wishlisted is true, and update wishlist Original product event is true`() {
        val similarSearchOriginalProduct = similarSearchViewModel.getOriginalProductLiveData().value

        similarSearchOriginalProduct?.isWishlisted.shouldBe(
            true,
            "Original Product is wishlisted should be true"
        )

        val updateWishlistOriginalProductEventLiveData = similarSearchViewModel.getUpdateWishlistOriginalProductEventLiveData().value

        updateWishlistOriginalProductEventLiveData?.getContentIfNotHandled().shouldBe(
            true,
            "Update wishlist Original product event should be true"
        )
    }

    @Test
    fun `Add WishlistV2 Original Product Failed`() {
        val userId = "123456"
        val similarProductModelCommon = getSimilarProductModelCommon()
        val originalProduct = similarProductModelCommon.getOriginalProduct()

        `Given user is logged in with user id`(userId)
        `Given view already created and has similar search data`(similarProductModelCommon)
        `Given add wishlistV2 API will fail`()

        `When handle view toggle wishlistV2 Original product`()
        `Then verify add wishlistV2 API is called with product id equals to Original product id`(originalProduct, userId)
    }

    private fun `Given add wishlistV2 API will fail`() {
        val mockThrowable = mockk<Throwable>("fail")
        every { addToWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { addToWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)
    }

    @Test
    fun `Remove WishlistV2 Original Product Success`() {
        val userId = "123456"
        val similarProductModelOriginalProductWishlisted = getSimilarProductModelOriginalProductWishlisted()
        val originalProduct = similarProductModelOriginalProductWishlisted.getOriginalProduct()

        `Given user is logged in with user id`(userId)

        `Given view already created and has similar search data`(similarProductModelOriginalProductWishlisted)
        `Given remove wishlistV2 API will be successful`()

        `When handle view toggle wishlistV2 Original product`()

        `Then verify remove wishlistV2 API is called with product id equals to Original product id`(originalProduct, userId)
        `Then assert remove wishlistV2 success field is true`()
        `Then assert Original product is wishlisted is false, and update wishlist Original product event is false`()
    }

    private fun `Given remove wishlistV2 API will be successful`() {
        val resultWishlistRemoveV2 = DeleteWishlistV2Response.Data.WishlistRemoveV2(success = true)
        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Success(resultWishlistRemoveV2)
    }

    private fun `Then assert Original product is wishlisted is false, and update wishlist Original product event is false`() {
        val similarSearchOriginalProduct = similarSearchViewModel.getOriginalProductLiveData().value

        similarSearchOriginalProduct?.isWishlisted.shouldBe(
            false,
            "Original Product is wishlisted should be false"
        )

        val updateWishlistOriginalProductEventLiveData = similarSearchViewModel.getUpdateWishlistOriginalProductEventLiveData().value

        updateWishlistOriginalProductEventLiveData?.getContentIfNotHandled().shouldBe(
            false,
            "Update wishlist Original product event should be false"
        )
    }

    @Test
    fun `Remove WishlistV2 Original Product Failed`() {
        val userId = "123456"
        val similarProductModelOriginalProductWishlisted = getSimilarProductModelOriginalProductWishlisted()
        val originalProduct = similarProductModelOriginalProductWishlisted.getOriginalProduct()

        `Given user is logged in with user id`(userId)
        `Given view already created and has similar search data`(similarProductModelOriginalProductWishlisted)
        `Given remove wishlistV2 API will fail`()

        `When handle view toggle wishlistV2 Original product`()

        `Then verify remove wishlistV2 API is called with product id equals to Original product id`(originalProduct, userId)
        `Then assert Original product is wishlisted stays true, and update wishlist Original product event is null`()
    }

    private fun `Given remove wishlistV2 API will fail`() {
        val mockThrowable = mockk<Throwable>("fail")

        every { deleteWishlistV2UseCase.setParams(any(), any()) } just Runs
        coEvery { deleteWishlistV2UseCase.executeOnBackground() } returns Fail(mockThrowable)
    }

    private fun `Then assert remove wishlist Original product event is false`() {
        val removeWishlistEventLiveData = similarSearchViewModel.getRemoveWishlistEventLiveData().value

        removeWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
            false,
            "Remove wishlist event should be false"
        )
    }

    private fun `Then assert Original product is wishlisted stays true, and update wishlist Original product event is null`() {
        val similarSearchOriginalProduct = similarSearchViewModel.getOriginalProductLiveData().value

        similarSearchOriginalProduct?.isWishlisted.shouldBe(
            true,
            "Original Product is wishlisted should be true"
        )

        val updateWishlistOriginalProductEventLiveData = similarSearchViewModel.getUpdateWishlistOriginalProductEventLiveData().value

        updateWishlistOriginalProductEventLiveData?.getContentIfNotHandled().shouldBe(
            null,
            "Update wishlist selected product event should be null"
        )
    }
}