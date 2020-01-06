package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.search.InstantTaskExecutorRuleSpek
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.search.shouldBe
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleViewClickProductPreviewTest: Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Handle view click product preview") {
        createTestInstance()

        Scenario("Click Product Preview") {
            val shopItemProduct = ShopViewModel.ShopItem.ShopItemProduct(
                    name = "Test product preview",
                    id = 12345,
                    price = 10000,
                    applink = "tokopedia://product/12345"
            ).also {
                it.position = 1
            }

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            When("View Click Product Preview") {
                searchShopViewModel.onViewClickProductPreview(shopItemProduct)
            }

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            When("View Click Shop") {
                searchShopViewModel.onViewClickProductPreview(shopItemProduct)
            }

            Then("should post route to shop page with applink") {
                val routePageEventLiveData = searchShopViewModel.getRoutePageEventLiveData().value

                routePageEventLiveData?.getContentIfNotHandled() shouldBe shopItemProduct.applink
            }

            Then("should post tracking click product preview") {
                val clickProductItemTrackingEventLiveData = searchShopViewModel.getClickProductItemTrackingEventLiveData().value

                clickProductItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe shopItemProduct
            }

            Then("should NOT post tracking click product preview recommendation") {
                val clickProductRecommendationItemTrackingEventLiveData = searchShopViewModel.getClickProductRecommendationItemTrackingEventLiveData().value

                clickProductRecommendationItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe null
            }
        }

        Scenario("Click Product Preview Recommendation") {
            val shopItemProduct = ShopViewModel.ShopItem.ShopItemProduct(
                    name = "Test product preview",
                    id = 12345,
                    price = 10000,
                    applink = "tokopedia://product/12345",
                    isRecommendation = true
            ).also {
                it.position = 1
            }

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            When("View Click Product Preview") {
                searchShopViewModel.onViewClickProductPreview(shopItemProduct)
            }

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            When("View Click Shop") {
                searchShopViewModel.onViewClickProductPreview(shopItemProduct)
            }

            Then("should post route to shop page with applink") {
                val routePageEventLiveData = searchShopViewModel.getRoutePageEventLiveData().value

                routePageEventLiveData?.getContentIfNotHandled() shouldBe shopItemProduct.applink
            }

            Then("should post tracking click product preview") {
                val clickProductItemTrackingEventLiveData = searchShopViewModel.getClickProductItemTrackingEventLiveData().value

                clickProductItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe null
            }

            Then("should NOT post tracking click product preview recommendation") {
                val clickProductRecommendationItemTrackingEventLiveData = searchShopViewModel.getClickProductRecommendationItemTrackingEventLiveData().value

                clickProductRecommendationItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe shopItemProduct
            }
        }
    }
})