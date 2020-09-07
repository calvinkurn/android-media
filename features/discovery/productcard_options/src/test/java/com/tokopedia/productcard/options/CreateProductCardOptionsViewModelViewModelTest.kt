package com.tokopedia.productcard.options

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.productcard.options.item.ProductCardOptionsItemModel
import com.tokopedia.productcard.options.testutils.shouldHaveSize
import org.junit.Test

internal class CreateProductCardOptionsViewModelViewModelTest: ProductCardOptionsViewModelTestFixtures() {

    @Test
    fun `Null Options`() {
        `When Create product Card Options View Model with null options`()
        `Then Assert product card options item model list is empty`()
    }

    private fun `When Create product Card Options View Model with null options`() {
        createProductCardOptionsViewModel(null)
    }

    private fun `Then Assert product card options item model list is empty`() {
        val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

        productCardOptionsItemModelList shouldHaveSize 0
    }

    @Test
    fun `Options Has Similar Search`() {
        `When Create Product Card Options View Model with hasSimilarSearch = true`()
        `Then Assert product card options item model list contains option to see similar products`()
    }

    private fun `When Create Product Card Options View Model with hasSimilarSearch = true`() {
        createProductCardOptionsViewModel(ProductCardOptionsModel(
                hasSimilarSearch = true
        ))
    }

    private fun `Then Assert product card options item model list contains option to see similar products`() {
        val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

        productCardOptionsItemModelList.shouldContain {
            it is ProductCardOptionsItemModel && it.title == SEE_SIMILAR_PRODUCTS
        }
    }

    @Test
    fun `Options Does not Have Similar Search`() {
        `When Create Product Card Options View Model with hasSimilarSearch = false`()
        `Then Assert product card options item model list does not contain option to see similar products`()
    }

    private fun `When Create Product Card Options View Model with hasSimilarSearch = false`() {
        createProductCardOptionsViewModel(ProductCardOptionsModel(
                hasSimilarSearch = false
        ))
    }

    private fun `Then Assert product card options item model list does not contain option to see similar products`() {
        val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

        productCardOptionsItemModelList.shouldNotContain {
            it is ProductCardOptionsItemModel && it.title == SEE_SIMILAR_PRODUCTS
        }
    }

    @Test
    fun `Options Has Wishlist and Is not Wishlisted`() {
        `When Create Product Card Options View Model with hasWishlist = true, and isWishlisted = false`()
        `Then Assert product card options item model list contains option to add to wishlist`()
    }

    private fun `When Create Product Card Options View Model with hasWishlist = true, and isWishlisted = false`() {
        createProductCardOptionsViewModel(ProductCardOptionsModel(
                hasWishlist = true,
                isWishlisted = false
        ))
    }

    private fun `Then Assert product card options item model list contains option to add to wishlist`() {
        val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

        productCardOptionsItemModelList.shouldContain {
            it is ProductCardOptionsItemModel && it.title == SAVE_TO_WISHLIST
        }
    }

    @Test
    fun `Options Has Wishlist and Is Wishlisted`() {
        `When Product Card Options View Model with hasWishlist = true, and isWishlisted = true`()
        `Then Assert product card options item model list contains option to delete from wishlist`()
    }

    private fun `When Product Card Options View Model with hasWishlist = true, and isWishlisted = true`() {
        createProductCardOptionsViewModel(ProductCardOptionsModel(
                hasWishlist = true,
                isWishlisted = true
        ))
    }

    private fun `Then Assert product card options item model list contains option to delete from wishlist`() {
        val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

        productCardOptionsItemModelList.shouldContain {
            it is ProductCardOptionsItemModel && it.title == DELETE_FROM_WISHLIST
        }
    }

    @Test
    fun `Options Does not Have Wishlist`() {
        `When Product Card Options View Model with hasWishlist = false`()
        `Then Assert product card options item model list does not contain option to save or remove wishlist`()
    }

    private fun `When Product Card Options View Model with hasWishlist = false`() {
        createProductCardOptionsViewModel(ProductCardOptionsModel(
                hasWishlist = false
        ))
    }

    private fun `Then Assert product card options item model list does not contain option to save or remove wishlist`() {
        val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

        productCardOptionsItemModelList.shouldNotContain {
            it is ProductCardOptionsItemModel
                    && (it.title == SAVE_TO_WISHLIST || it.title == DELETE_FROM_WISHLIST)
        }
    }

    @Test
    fun `Has Multiple Options`() {
        `When Product Card Options View Model with hasSimilarSearch = true, and hasWishlist = true`()
        `Then Assert product card options item model list has divider item model in between options`()
    }

    private fun `When Product Card Options View Model with hasSimilarSearch = true, and hasWishlist = true`() {
        createProductCardOptionsViewModel(ProductCardOptionsModel(
                hasSimilarSearch = true,
                hasWishlist = true
        ))
    }

    private fun `Then Assert product card options item model list has divider item model in between options`() {
        val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

        productCardOptionsItemModelList!!.forEachIndexed { index, item ->
            if (index % 2 == 0) item.shouldBeInstanceOf<ProductCardOptionsItemModel>()
            else item.shouldBeInstanceOf<ProductCardOptionsItemDivider>()
        }
    }
}