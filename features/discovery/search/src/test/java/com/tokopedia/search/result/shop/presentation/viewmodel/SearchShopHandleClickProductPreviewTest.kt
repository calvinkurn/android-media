package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.search.shouldBe
import org.junit.Test

internal class SearchShopHandleClickProductPreviewTest: SearchShopViewModelTestFixtures() {

    @Test
    fun `Click Product Preview`() {
        val shopItemProduct = ShopViewModel.ShopItem.ShopItemProduct(
                name = "Test product preview",
                id = 12345,
                price = 10000,
                applink = "tokopedia://product/12345"
        ).also {
            it.position = 1
        }

        `When View Click Product Preview`(shopItemProduct)

        `Then should post route to product page with applink`(shopItemProduct)
        `Then should post tracking click product preview`(shopItemProduct)
        `Then should NOT post tracking click product preview recommendation`()
    }

    private fun `When View Click Product Preview`(shopItemProduct: ShopViewModel.ShopItem.ShopItemProduct) {
        searchShopViewModel.onViewClickProductPreview(shopItemProduct)
    }

    private fun `Then should post route to product page with applink`(shopItemProduct: ShopViewModel.ShopItem.ShopItemProduct) {
        val routePageEventLiveData = searchShopViewModel.getRoutePageEventLiveData().value

        routePageEventLiveData?.getContentIfNotHandled() shouldBe shopItemProduct.applink
    }

    private fun `Then should post tracking click product preview`(shopItemProduct: ShopViewModel.ShopItem.ShopItemProduct) {
        val clickProductItemTrackingEventLiveData = searchShopViewModel.getClickProductItemTrackingEventLiveData().value

        clickProductItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe shopItemProduct
    }

    private fun `Then should NOT post tracking click product preview recommendation`() {
        val clickProductRecommendationItemTrackingEventLiveData = searchShopViewModel.getClickProductRecommendationItemTrackingEventLiveData().value

        clickProductRecommendationItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe null
    }

    @Test
    fun `Click Product Preview Recommendation`() {
        val shopItemProduct = ShopViewModel.ShopItem.ShopItemProduct(
                name = "Test product preview",
                id = 12345,
                price = 10000,
                applink = "tokopedia://product/12345",
                isRecommendation = true
        ).also {
            it.position = 1
        }

        `When View Click Product Preview`(shopItemProduct)

        `Then should post route to product page with applink`(shopItemProduct)
        `Then should NOT post tracking click product preview`()
        `Then should post tracking click product preview recommendation`(shopItemProduct)
    }

    private fun `Then should NOT post tracking click product preview`() {
        val clickProductItemTrackingEventLiveData = searchShopViewModel.getClickProductItemTrackingEventLiveData().value

        clickProductItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe null
    }

    private fun `Then should post tracking click product preview recommendation`(shopItemProduct: ShopViewModel.ShopItem.ShopItemProduct) {
        val clickProductRecommendationItemTrackingEventLiveData = searchShopViewModel.getClickProductRecommendationItemTrackingEventLiveData().value

        clickProductRecommendationItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe shopItemProduct
    }
}