package com.tokopedia.productcard.options

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import io.mockk.every
import org.junit.Test

internal class ClickAddToCartOptionsTest: ProductCardOptionsViewModelTestFixtures() {

    private val productCardOptionsModelATC = ProductCardOptionsModel(
            hasAddToCart = true,
            productId = "12345",
            productName = "Product Name",
            shopId = "123456",
            categoryName = "Handphone",
            price = "Rp32.900",
            addToCartParams = ProductCardOptionsModel.AddToCartParams(quantity = 1)
    )

    @Test
    fun `Click add to cart should redirect to login page for non login user`() {
        `Given Product Card Options View Model with ATC enabled`()
        `Given User is not logged in`()

        `When click add to cart`()

        `Then should redirect to login page`()
    }

    private fun `Given Product Card Options View Model with ATC enabled`() {
        createProductCardOptionsViewModel(productCardOptionsModelATC)
    }

    private fun `Given User is not logged in`() {
        every { userSession.isLoggedIn }.returns(false)
    }

    private fun `When click add to cart`() {
        productCardOptionsViewModel.getOption(ADD_TO_CART).onClick()
    }

    private fun `Then should redirect to login page`() {
        val isRouteToLoginPage = productCardOptionsViewModel.routeToLoginPageEventLiveData().value?.getContentIfNotHandled()

        isRouteToLoginPage.shouldBe(true, "Should route to login page.")
    }

    @Test
    fun `Click add to cart `() {
        `Given Product Card Options View Model with ATC enabled`()
        `Given User is not logged in`()

        `When click add to cart`()

        `Then should redirect to login page`()
    }
}