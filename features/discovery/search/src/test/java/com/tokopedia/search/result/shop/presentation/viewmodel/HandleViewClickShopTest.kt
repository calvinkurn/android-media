package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchConstant.ShopStatus.*
import com.tokopedia.search.InstantTaskExecutorRuleSpek
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.search.shouldBe
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

internal class HandleViewClickShopTest: Spek({
    InstantTaskExecutorRuleSpek(this)

    Feature("Handle View Click Shop") {
        createTestInstance()

        Scenario("Click Active Shop") {
            val shopItem = ShopViewModel.ShopItem(
                    id = "12345",
                    name = "Shop Name",
                    status = 1,
                    applink = "tokopedia://shop/12345"
            ).also {
                it.position = 1
            }

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            When("View Click Shop") {
                searchShopViewModel.onViewClickShop(shopItem)
            }

            Then("should post route to shop page with applink") {
                val routePageEventLiveData = searchShopViewModel.getRoutePageEventLiveData().value

                routePageEventLiveData?.getContentIfNotHandled() shouldBe shopItem.applink
            }

            Then("should post tracking click shop item") {
                val clickShopItemTrackingEventLiveData = searchShopViewModel.getClickShopItemTrackingEventLiveData().value

                clickShopItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe shopItem
            }

            Then("should NOT post tracking click not active shop item") {
                val clickNotActiveShopItemTrackingEventLiveData =
                        searchShopViewModel.getClickNotActiveShopItemTrackingEventLiveData().value

                clickNotActiveShopItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe null
            }

            Then("should NOT post tracking click shop recommendation item") {
                val clickShopRecommendationItemTrackingEventLiveData =
                        searchShopViewModel.getClickShopRecommendationItemTrackingEventLiveData().value

                clickShopRecommendationItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe null
            }
        }

        Scenario("Click Closed Shop (status = $KEY_SHOP_STATUS_CLOSED)") {
            val shopItem = ShopViewModel.ShopItem(
                    id = "12345",
                    name = "Shop Name",
                    status = KEY_SHOP_STATUS_CLOSED,
                    applink = "tokopedia://shop/12345"
            ).also {
                it.position = 1
            }

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            When("View Click Shop") {
                searchShopViewModel.onViewClickShop(shopItem)
            }

            Then("should post route to shop page with applink") {
                val routePageEventLiveData = searchShopViewModel.getRoutePageEventLiveData().value

                routePageEventLiveData?.getContentIfNotHandled() shouldBe shopItem.applink
            }

            Then("should post tracking click shop item") {
                val clickShopItemTrackingEventLiveData = searchShopViewModel.getClickShopItemTrackingEventLiveData().value

                clickShopItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe shopItem
            }

            Then("should post tracking click not active shop item") {
                val clickNotActiveShopItemTrackingEventLiveData =
                        searchShopViewModel.getClickNotActiveShopItemTrackingEventLiveData().value

                clickNotActiveShopItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe shopItem
            }

            Then("should NOT post tracking click shop recommendation item") {
                val clickShopRecommendationItemTrackingEventLiveData =
                        searchShopViewModel.getClickShopRecommendationItemTrackingEventLiveData().value

                clickShopRecommendationItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe null
            }
        }

        Scenario("Click Moderated Shop (status = $KEY_SHOP_STATUS_MODERATED)") {
            val shopItem = ShopViewModel.ShopItem(
                    id = "12345",
                    name = "Shop Name",
                    status = KEY_SHOP_STATUS_MODERATED,
                    applink = "tokopedia://shop/12345"
            ).also {
                it.position = 1
            }

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            When("View Click Shop") {
                searchShopViewModel.onViewClickShop(shopItem)
            }

            Then("should post route to shop page with applink") {
                val routePageEventLiveData = searchShopViewModel.getRoutePageEventLiveData().value

                routePageEventLiveData?.getContentIfNotHandled() shouldBe shopItem.applink
            }

            Then("should post tracking click shop item") {
                val clickShopItemTrackingEventLiveData = searchShopViewModel.getClickShopItemTrackingEventLiveData().value

                clickShopItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe shopItem
            }

            Then("should post tracking click not active shop item") {
                val clickNotActiveShopItemTrackingEventLiveData =
                        searchShopViewModel.getClickNotActiveShopItemTrackingEventLiveData().value

                clickNotActiveShopItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe shopItem
            }

            Then("should NOT post tracking click shop recommendation item") {
                val clickShopRecommendationItemTrackingEventLiveData =
                        searchShopViewModel.getClickShopRecommendationItemTrackingEventLiveData().value

                clickShopRecommendationItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe null
            }
        }

        Scenario("Click Inactive Shop (status = $KEY_SHOP_STATUS_INACTIVE)") {
            val shopItem = ShopViewModel.ShopItem(
                    id = "12345",
                    name = "Shop Name",
                    status = KEY_SHOP_STATUS_INACTIVE,
                    applink = "tokopedia://shop/12345"
            ).also {
                it.position = 1
            }

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            When("View Click Shop") {
                searchShopViewModel.onViewClickShop(shopItem)
            }

            Then("should post route to shop page with applink") {
                val routePageEventLiveData = searchShopViewModel.getRoutePageEventLiveData().value

                routePageEventLiveData?.getContentIfNotHandled() shouldBe shopItem.applink
            }

            Then("should post tracking click shop item") {
                val clickShopItemTrackingEventLiveData = searchShopViewModel.getClickShopItemTrackingEventLiveData().value

                clickShopItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe shopItem
            }

            Then("should post tracking click not active shop item") {
                val clickNotActiveShopItemTrackingEventLiveData =
                        searchShopViewModel.getClickNotActiveShopItemTrackingEventLiveData().value

                clickNotActiveShopItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe shopItem
            }

            Then("should NOT post tracking click shop recommendation item") {
                val clickShopRecommendationItemTrackingEventLiveData =
                        searchShopViewModel.getClickShopRecommendationItemTrackingEventLiveData().value

                clickShopRecommendationItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe null
            }
        }

        Scenario("Click Shop Recommendation") {
            val shopItem = ShopViewModel.ShopItem(
                    id = "12345",
                    name = "Shop Recommendation Name",
                    status = 1,
                    applink = "tokopedia://shop/12345",
                    isRecommendation = true
            ).also {
                it.position = 1
            }

            lateinit var searchShopViewModel: SearchShopViewModel

            Given("search shop view model") {
                searchShopViewModel = createSearchShopViewModel()
            }

            When("View Click Shop") {
                searchShopViewModel.onViewClickShop(shopItem)
            }

            Then("should post route to shop page with applink") {
                val routePageEventLiveData = searchShopViewModel.getRoutePageEventLiveData().value

                routePageEventLiveData?.getContentIfNotHandled() shouldBe shopItem.applink
            }

            Then("should NOT post tracking click shop item") {
                val clickShopItemTrackingEventLiveData = searchShopViewModel.getClickShopItemTrackingEventLiveData().value

                clickShopItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe null
            }

            Then("should NOT post tracking click not active shop item") {
                val clickNotActiveShopItemTrackingEventLiveData =
                        searchShopViewModel.getClickNotActiveShopItemTrackingEventLiveData().value

                clickNotActiveShopItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe null
            }

            Then("should post tracking click shop recommendation item") {
                val clickShopRecommendationItemTrackingEventLiveData =
                        searchShopViewModel.getClickShopRecommendationItemTrackingEventLiveData().value

                clickShopRecommendationItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe shopItem
            }
        }
    }
})