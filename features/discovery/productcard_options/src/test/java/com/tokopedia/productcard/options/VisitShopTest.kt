package com.tokopedia.productcard.options

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import org.junit.Test

internal class VisitShopTest: ProductCardOptionsViewModelTestFixtures() {

    private val productCardOptionModelVisitShop = ProductCardOptionsModel(
            hasVisitShop = true,
            shop = ProductCardOptionsModel.Shop(shopId = "12345")
    )

    @Test
    fun `Click visit shop`() {
        `Given product card options with visit shop`()

        `When click visit shop`()

        `Then should route to shop page`()
    }

    private fun `Given product card options with visit shop`() {
        createProductCardOptionsViewModel(productCardOptionModelVisitShop)
    }

    private fun `When click visit shop`() {
        productCardOptionsViewModel.getOption(VISIT_SHOP).onClick()
    }

    private fun `Then should route to shop page`() {
        productCardOptionsViewModel.getRouteToShopPageEventLiveData().value?.getContentIfNotHandled() shouldBe true
    }
}