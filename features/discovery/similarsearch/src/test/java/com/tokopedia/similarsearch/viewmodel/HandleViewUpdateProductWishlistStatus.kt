package com.tokopedia.similarsearch.viewmodel

import com.tokopedia.similarsearch.getsimilarproducts.model.Product
import com.tokopedia.similarsearch.testutils.shouldBe
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelCommon
import com.tokopedia.similarsearch.viewmodel.testinstance.getSimilarProductModelOriginalProductWishlisted
import org.junit.Test

internal class HandleViewUpdateProductWishlistStatus: SimilarSearchTestFixtures() {

    @Test
    fun `Degenerate cases`() {
        `Given view already created and has similar search data`(getSimilarProductModelCommon())

        `When handle view update product wishlist status with degenerate product id`()

        `Then update wishlist selected product event is null`()
    }

    private fun `When handle view update product wishlist status with degenerate product id`() {
        similarSearchViewModel.onViewUpdateProductWishlistStatus(null, true)
        similarSearchViewModel.onViewUpdateProductWishlistStatus(null, false)
        similarSearchViewModel.onViewUpdateProductWishlistStatus("", true)
        similarSearchViewModel.onViewUpdateProductWishlistStatus("", false)
        similarSearchViewModel.onViewUpdateProductWishlistStatus("randomproductid", true)
        similarSearchViewModel.onViewUpdateProductWishlistStatus("randomproductid", false)
    }

    private fun `Then update wishlist selected product event is null`() {
        val updateWishlistSelectedProductEventLiveData = similarSearchViewModel.getUpdateWishlistOriginalProductEventLiveData().value

        updateWishlistSelectedProductEventLiveData?.getContentIfNotHandled().shouldBe(
            null,
            "Update wishlist selected product event should be null"
        )
    }

    @Test
    fun `Wishlist updated for original product to true, and original product isWishlisted already true`() {
        val similarProductModelOriginalProductWishlisted = getSimilarProductModelOriginalProductWishlisted()
        val originalProduct = similarProductModelOriginalProductWishlisted.getOriginalProduct()

        `Given view already created and has similar search data`(similarProductModelOriginalProductWishlisted)

        `When handle view update product wishlist status`(originalProduct, true)

        `Then assert original product isWishlisted is true, and update wishlist original product event is null`()
    }

    private fun `When handle view update product wishlist status`(
        product: Product,
        isWishlisted: Boolean
    ) {
        similarSearchViewModel.onViewUpdateProductWishlistStatus(product.id, isWishlisted)
    }

    private fun `Then assert original product isWishlisted is true, and update wishlist original product event is null`() {
        val similarSearchOriginalProduct = similarSearchViewModel.getOriginalProductLiveData().value

        similarSearchOriginalProduct?.isWishlisted.shouldBe(
            true,
            "Original Product is wishlisted should be true"
        )

        val updateWishlistOriginalProductEventLiveData = similarSearchViewModel.getUpdateWishlistOriginalProductEventLiveData().value

        updateWishlistOriginalProductEventLiveData?.getContentIfNotHandled().shouldBe(
            null,
            "Update wishlist Original product event should be null"
        )
    }

    @Test
    fun `Wishlist updated for Original product to false, and Original product isWishlisted already false`() {
        val similarProductModelCommon = getSimilarProductModelCommon()
        val originalProduct = similarProductModelCommon.getOriginalProduct()

        `Given view already created and has similar search data`(similarProductModelCommon)

        `When handle view update product wishlist status`(originalProduct, false)

        `Then assert Original product isWishlisted is false, and update wishlist Original product event is null`()
    }

    private fun `Then assert Original product isWishlisted is false, and update wishlist Original product event is null`() {
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
    fun `Wishlist updated for Original product to true`() {
        val similarProductModelCommon = getSimilarProductModelCommon()
        val originalProduct = similarProductModelCommon.getOriginalProduct()

        `Given view already created and has similar search data`(similarProductModelCommon)

        `When handle view update product wishlist status`(originalProduct, true)

        `Then assert Original product isWishlisted is true, and update wishlist Original product event is true`()
    }

    private fun `Then assert Original product isWishlisted is true, and update wishlist Original product event is true`() {
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
    fun `Wishlist updated for Original product to false`() {
        val similarProductModelOriginalProductWishlisted = getSimilarProductModelOriginalProductWishlisted()
        val originalProduct = similarProductModelOriginalProductWishlisted.getOriginalProduct()

        `Given view already created and has similar search data`(similarProductModelOriginalProductWishlisted)

        `When handle view update product wishlist status`(originalProduct, false)

        `Then assert Original product isWishlisted is false, and update wishlist Original product event is false`()
    }

    private fun `Then assert Original product isWishlisted is false, and update wishlist Original product event is false`() {
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
}