package com.tokopedia.similarsearch.viewmodel

import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import com.tokopedia.similarsearch.testutils.shouldBe
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelCommon
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelOriginalProductWishlisted
import com.tokopedia.wishlist.common.listener.WishListActionListener
import io.mockk.every
import io.mockk.verify
import org.junit.Test

internal class HandleViewToggleWishlistOriginalProductTest: SimilarSearchTestFixtures() {
    
    @Test
    fun `Handle View Toggle Wishlist Original Product for non-login user`() {
        val similarProductModelCommon = getSimilarProductModelCommon()

        `Given user is not logged in`()
        `Given view already created and has similar search data`(similarProductModelCommon)

        `When handle view toggle wishlist Original product`()

        `Then should post event go to login page`()
    }

    private fun `When handle view toggle wishlist Original product`() {
        similarSearchViewModel.onViewToggleWishlistOriginalProduct()
    }

    private fun `Then should post event go to login page`() {
        val routeToLoginEvent = similarSearchViewModel.getRouteToLoginPageEventLiveData().value

        routeToLoginEvent?.getContentIfNotHandled().shouldBe(true,
            "Route to login page should be true")
    }

    @Test
    fun `Handle View Toggle Wishlist Original Product for logged in user and product is not wishlisted`() {
        val userId = "123456"
        val similarProductModelCommon = getSimilarProductModelCommon()
        val originalProduct = similarProductModelCommon.getOriginalProduct()

        `Given view already created and has similar search data`(similarProductModelCommon)
        `Given user is logged in with user id`(userId)

        `When handle view toggle wishlist Original product`()

        `Then verify add wishlist API is called with product id equals to Original product id`(
            originalProduct, userId
        )
    }

    private fun `Then verify add wishlist API is called with product id equals to Original product id`(
        originalProduct: Product,
        userId: String
    ) {
        verify(exactly = 1) {
            addWishListUseCase.createObservable(originalProduct.id, userId, any())
        }
    }

    @Test
    fun `Handle View Toggle Wishlist Original Product for logged in user and product is wishlisted`() {
        val userId = "123456"
        val similarProductModelOriginalProductWishlisted = getSimilarProductModelOriginalProductWishlisted()
        val originalProduct = similarProductModelOriginalProductWishlisted.getOriginalProduct()

        `Given user is logged in with user id`(userId)
        `Given view already created and has similar search data`(similarProductModelOriginalProductWishlisted)

        `When handle view toggle wishlist Original product`()

        `Then verify remove wishlist API is called with product id equals to Original product id`(
            originalProduct, userId
        )
    }

    private fun `Then verify remove wishlist API is called with product id equals to Original product id`(
        originalProduct: Product,
        userId: String
    ) {
        verify(exactly = 1) {
            removeWishListUseCase.createObservable(originalProduct.id, userId, any()) }
    }

    @Test
    fun `Add Wishlist Original Product Success`() {
        val userId = "123456"
        val similarProductModelCommon = getSimilarProductModelCommon()
        val originalProduct = similarProductModelCommon.getOriginalProduct()

        `Given user is logged in with user id`(userId)

        `Given view already created and has similar search data`(similarProductModelCommon)
        `Given add wishlist API will be successful`(originalProduct, userId)

        `When handle view toggle wishlist Original product`()

        `Then assert add wishlist event is true`()
        `Then assert Original product is wishlisted is true, and update wishlist Original product event is true`()
    }

    private fun `Given add wishlist API will be successful`(
        originalProduct: Product,
        userId: String
    ) {
        every {
            addWishListUseCase.createObservable(originalProduct.id, userId, any())
        }.answers {
            thirdArg<WishListActionListener>().onSuccessAddWishlist(firstArg())
        }
    }

    private fun `Then assert add wishlist event is true`() {
        val addWishlistEventLiveData = similarSearchViewModel.getAddWishlistEventLiveData().value

        addWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
            true,
            "Add wishlist event should be true"
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
    fun `Add Wishlist Original Product Failed`() {
        val userId = "123456"
        val similarProductModelCommon = getSimilarProductModelCommon()
        val originalProduct = similarProductModelCommon.getOriginalProduct()

        `Given user is logged in with user id`(userId)
        `Given view already created and has similar search data`(similarProductModelCommon)
        `Given add wishlist API will fail`(originalProduct, userId)

        `When handle view toggle wishlist Original product`()

        `Then assert add wishlist event is false`()
        `Then assert Original product is wishlisted stays false, and update wishlist Original product event is null`()
    }

    private fun `Given add wishlist API will fail`(
        originalProduct: Product,
        userId: String
    ) {
        every {
            addWishListUseCase.createObservable(originalProduct.id, userId, any())
        }.answers {
            thirdArg<WishListActionListener>().onErrorAddWishList("error from backend", firstArg())
        }
    }

    private fun `Then assert add wishlist event is false`() {
        val addWishlistEventLiveData = similarSearchViewModel.getAddWishlistEventLiveData().value

        addWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
            false,
            "Add wishlist event should be false"
        )
    }

    private fun `Then assert Original product is wishlisted stays false, and update wishlist Original product event is null`() {
        val similarSearchOriginalProduct = similarSearchViewModel.getOriginalProductLiveData().value

        similarSearchOriginalProduct?.isWishlisted.shouldBe(
            false,
            "Original Product is wishlisted should be false"
        )

        val updateWishlistOriginalProductEventLiveData = similarSearchViewModel.getUpdateWishlistOriginalProductEventLiveData().value

        updateWishlistOriginalProductEventLiveData?.getContentIfNotHandled().shouldBe(
            null,
            "Update wishlist Original product event should be null"
        )
    }
    
    @Test
    fun `Remove Wishlist Original Product Success`() {
        val userId = "123456"
        val similarProductModelOriginalProductWishlisted = getSimilarProductModelOriginalProductWishlisted()
        val originalProduct = similarProductModelOriginalProductWishlisted.getOriginalProduct()

        `Given user is logged in with user id`(userId)
        `Given view already created and has similar search data`(similarProductModelOriginalProductWishlisted)
        `Given remove wishlist API will be successful`(originalProduct, userId)

        `When handle view toggle wishlist Original product`()

        `Then assert remove wishlist event is true`()
        `Then assert Original product is wishlisted is false, and update wishlist Original product event is false`()
    }

    private fun `Given remove wishlist API will be successful`(
        originalProduct: Product,
        userId: String
    ) {
        every {
            removeWishListUseCase.createObservable(originalProduct.id, userId, any())
        }.answers {
            thirdArg<WishListActionListener>().onSuccessRemoveWishlist(firstArg())
        }
    }

    private fun `Then assert remove wishlist event is true`() {
        val removeWishlistEventLiveData = similarSearchViewModel.getRemoveWishlistEventLiveData().value

        removeWishlistEventLiveData?.getContentIfNotHandled().shouldBe(
            true,
            "Remove wishlist event should be true"
        )
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
    fun `Remove Wishlist Original Product Failed`() {
        val userId = "123456"
        val similarProductModelOriginalProductWishlisted = getSimilarProductModelOriginalProductWishlisted()
        val originalProduct = similarProductModelOriginalProductWishlisted.getOriginalProduct()

        `Given user is logged in with user id`(userId)
        `Given view already created and has similar search data`(similarProductModelOriginalProductWishlisted)
        `Given remove wishlist API will fail`(originalProduct, userId)

        `When handle view toggle wishlist Original product`()

        `Then assert remove wishlist Original product event is false`()
        `Then assert Original product is wishlisted stays true, and update wishlist Original product event is null`()
    }

    private fun `Given remove wishlist API will fail`(
        originalProduct: Product,
        userId: String
    ) {
        every {
            removeWishListUseCase.createObservable(originalProduct.id, userId, any())
        }.answers {
            thirdArg<WishListActionListener>().onErrorRemoveWishlist(
                "error from backend",
                firstArg()
            )
        }
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