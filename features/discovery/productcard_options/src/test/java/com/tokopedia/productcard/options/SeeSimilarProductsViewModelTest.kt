package com.tokopedia.productcard.options

import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import org.junit.Test

internal class SeeSimilarProductsViewModelTest: ProductCardOptionsViewModelTestFixtures() {

    private val productCardOptionsModel = ProductCardOptionsModel(hasSimilarSearch = true)
    
    @Test
    fun `Click see similar products`() {
        `Given Product Card Options View Model`()

        `When Click see similar products`()

        `Then assert tracking click see similar product event is true`()
        `Then Route to Similar Search Event Live Data is true`()
        `Then Close Product Card Options Event Live Data is true`()
    }

    private fun `Given Product Card Options View Model`() {
        createProductCardOptionsViewModel(productCardOptionsModel)
    }

    private fun `When Click see similar products`() {
        productCardOptionsViewModel.getOption(SEE_SIMILAR_PRODUCTS).onClick()
    }

    private fun `Then assert tracking click see similar product event is true`() {
        val trackingSeeSimilarProductEventLiveData = productCardOptionsViewModel.getTrackingSeeSimilarProductEventLiveData().value

        trackingSeeSimilarProductEventLiveData?.getContentIfNotHandled() shouldBe true
    }

    private fun `Then Route to Similar Search Event Live Data is true`() {
        val routeToSimilarSearchEvent = productCardOptionsViewModel.getRouteToSimilarSearchEventLiveData().value

        routeToSimilarSearchEvent?.getContentIfNotHandled() shouldBe true
    }

    private fun `Then Close Product Card Options Event Live Data is true`() {
        val closeProductCardOptionsEvent = productCardOptionsViewModel.getCloseProductCardOptionsEventLiveData().value

        closeProductCardOptionsEvent?.getContentIfNotHandled() shouldBe true
    }
}