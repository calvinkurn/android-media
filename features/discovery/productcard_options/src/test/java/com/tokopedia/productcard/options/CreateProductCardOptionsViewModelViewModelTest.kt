package com.tokopedia.productcard.options

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.productcard.options.divider.ProductCardOptionsItemDivider
import com.tokopedia.productcard.options.item.ProductCardOptionsItemModel
import com.tokopedia.productcard.options.testutils.shouldHaveSize
import org.junit.Test

internal class CreateProductCardOptionsViewModelViewModelTest: ProductCardOptionsViewModelTestFixtures() {

    @Test
    fun `Null Options`() {
        `When Create Product Card Options View Model`(null)
        `Then Assert product card options item model list is empty`()
    }

    private fun `When Create Product Card Options View Model`(productCardOptionsModel: ProductCardOptionsModel?) {
        createProductCardOptionsViewModel(productCardOptionsModel)
    }

    private fun `Then Assert product card options item model list is empty`() {
        val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

        productCardOptionsItemModelList shouldHaveSize 0
    }

    @Test
    fun `Options Has Similar Search`() {
        `When Create Product Card Options View Model`(ProductCardOptionsModel(
                hasSimilarSearch = true
        ))
        `Then Assert product card options item model list contains option to see similar products`()
    }

    private fun `Then Assert product card options item model list contains option to see similar products`() {
        val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

        productCardOptionsItemModelList.shouldContain {
            it is ProductCardOptionsItemModel && it.title == SEE_SIMILAR_PRODUCTS
        }
    }

    @Test
    fun `Options Does not Have Similar Search`() {
        `When Create Product Card Options View Model`(ProductCardOptionsModel(
                hasSimilarSearch = false
        ))
        `Then Assert product card options item model list does not contain option to see similar products`()
    }

    private fun `Then Assert product card options item model list does not contain option to see similar products`() {
        val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

        productCardOptionsItemModelList.shouldNotContain {
            it is ProductCardOptionsItemModel && it.title == SEE_SIMILAR_PRODUCTS
        }
    }

    @Test
    fun `Options Has Wishlist and Is not Wishlisted`() {
        `When Create Product Card Options View Model`(ProductCardOptionsModel(
                hasWishlist = true,
                isWishlisted = false
        ))
        `Then Assert product card options item model list contains option to add to wishlist`()
    }

    private fun `Then Assert product card options item model list contains option to add to wishlist`() {
        val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

        productCardOptionsItemModelList.shouldContain {
            it is ProductCardOptionsItemModel && it.title == SAVE_TO_WISHLIST
        }
    }

    @Test
    fun `Options Has Wishlist and Is Wishlisted`() {
        `When Create Product Card Options View Model`(ProductCardOptionsModel(
                hasWishlist = true,
                isWishlisted = true
        ))
        `Then Assert product card options item model list contains option to delete from wishlist`()
    }

    private fun `Then Assert product card options item model list contains option to delete from wishlist`() {
        val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

        productCardOptionsItemModelList.shouldContain {
            it is ProductCardOptionsItemModel && it.title == DELETE_FROM_WISHLIST
        }
    }

    @Test
    fun `Options Does not Have Wishlist`() {
        `When Create Product Card Options View Model`(ProductCardOptionsModel(
                hasWishlist = false
        ))
        `Then Assert product card options item model list does not contain option to save or remove wishlist`()
    }

    private fun `Then Assert product card options item model list does not contain option to save or remove wishlist`() {
        val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

        productCardOptionsItemModelList.shouldNotContain {
            it is ProductCardOptionsItemModel
                    && (it.title == SAVE_TO_WISHLIST || it.title == DELETE_FROM_WISHLIST)
        }
    }

    @Test
    fun `Options has add to cart does not have ATC params`() {
        `When Create Product Card Options View Model`(ProductCardOptionsModel(
                hasAddToCart = true
        ))

        `Then assert product card options does not contain`(ADD_TO_CART)
    }

    private fun `Then assert product card options does not contain`(optionName: String) {
        val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

        productCardOptionsItemModelList.shouldNotContain {
            it is ProductCardOptionsItemModel && (it.title == optionName)
        }
    }

    @Test
    fun `Options has add to cart with all required ATC params`() {
        `When Create Product Card Options View Model`(ProductCardOptionsModel(
                hasAddToCart = true,
                productId = "12345",
                productName = "Product Name",
                shop = ProductCardOptionsModel.Shop(shopId = "12345"),
                categoryName = "Handphone",
                formattedPrice = "Rp32.900",
                addToCartParams = ProductCardOptionsModel.AddToCartParams(quantity = 1)
        ))

        `Then assert product card options contains`(ADD_TO_CART)
    }

    private fun `Then assert product card options contains`(optionName: String) {
        val productCardOptionsItemModelList = productCardOptionsViewModel.getOptionsListLiveData().value

        productCardOptionsItemModelList.shouldContain {
            it is ProductCardOptionsItemModel && (it.title == optionName)
        }
    }

    @Test
    fun `Options has visit shop without shop id`() {
        `When Create Product Card Options View Model`(ProductCardOptionsModel(
                hasVisitShop = true
        ))

        `Then assert product card options does not contain`(VISIT_SHOP)
    }

    @Test
    fun `Options has visit shop with shop id`() {
        `When Create Product Card Options View Model`(ProductCardOptionsModel(
                hasVisitShop = true,
                shop = ProductCardOptionsModel.Shop(shopId = "12345")
        ))

        `Then assert product card options contains`(VISIT_SHOP)
    }
    
    @Test
    fun `Option has share product without required parameters`() {
        `When Create Product Card Options View Model`(ProductCardOptionsModel(
                hasShareProduct = true
        ))

        `Then assert product card options does not contain`(SHARE_PRODUCT)
    }

    @Test
    fun `Options has share product with required parameters`() {
        `When Create Product Card Options View Model`(ProductCardOptionsModel(
                hasShareProduct = true,
                productId = "12345",
                productName = "Product name",
                productImageUrl = "https://imageurl",
                productUrl = "https://producturl",
                formattedPrice = "Rp1.000.000",
                shop = ProductCardOptionsModel.Shop(shopName = "Shop Name", shopUrl = "https://shopurl")
        ))

        `Then assert product card options contains`(SHARE_PRODUCT)
    }

    @Test
    fun `Has Multiple Options`() {
        `When Product Card Options View Model with hasSimilarSearch = true, and hasWishlist = true`()
        `Then Assert product card options item model list has divider item model in between options`()
    }

    private fun `When Product Card Options View Model with hasSimilarSearch = true, and hasWishlist = true`() {
        createProductCardOptionsViewModel(ProductCardOptionsModel(
                hasSimilarSearch = true,
                hasWishlist = true,
                hasAddToCart = true,
                hasVisitShop = true,
                hasShareProduct = true,
                categoryName = "Handphone",
                addToCartParams = ProductCardOptionsModel.AddToCartParams(quantity = 1),
                productId = "12345",
                productName = "Product name",
                productImageUrl = "https://imageurl",
                productUrl = "https://producturl",
                formattedPrice = "Rp1.000.000",
                shop = ProductCardOptionsModel.Shop(shopId = "12345", shopName = "Shop Name", shopUrl = "https://shopurl")
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