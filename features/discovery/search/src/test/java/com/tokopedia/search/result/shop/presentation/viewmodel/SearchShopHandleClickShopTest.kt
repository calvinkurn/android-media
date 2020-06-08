package com.tokopedia.search.result.shop.presentation.viewmodel

import com.tokopedia.discovery.common.constants.SearchConstant
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.search.shouldBe
import org.junit.Test

internal class SearchShopHandleClickShopTest: SearchShopViewModelTestFixtures() {

    @Test
    fun `Click Active Shop`() {
        val shopItem = ShopViewModel.ShopItem(
                id = "12345",
                name = "Shop Name",
                status = 1,
                applink = "tokopedia://shop/12345"
        ).also {
            it.position = 1
        }

        `When View Click Shop`(shopItem)

        `Then should post route to shop page with applink`(shopItem)
        `Then should post tracking click shop item`(shopItem)
        `Then should NOT post tracking click not active shop item`()
        `Then should NOT post tracking click shop recommendation item`()
    }

    private fun `When View Click Shop`(shopItem: ShopViewModel.ShopItem) {
        searchShopViewModel.onViewClickShop(shopItem)
    }

    private fun `Then should post route to shop page with applink`(shopItem: ShopViewModel.ShopItem) {
        val routePageEventLiveData = searchShopViewModel.getRoutePageEventLiveData().value

        routePageEventLiveData?.getContentIfNotHandled() shouldBe shopItem.applink
    }

    private fun `Then should post tracking click shop item`(shopItem: ShopViewModel.ShopItem) {
        val clickShopItemTrackingEventLiveData = searchShopViewModel.getClickShopItemTrackingEventLiveData().value

        clickShopItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe shopItem
    }

    private fun `Then should NOT post tracking click not active shop item`() {
        val clickNotActiveShopItemTrackingEventLiveData =
                searchShopViewModel.getClickNotActiveShopItemTrackingEventLiveData().value

        clickNotActiveShopItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe null
    }

    private fun `Then should NOT post tracking click shop recommendation item`() {
        val clickShopRecommendationItemTrackingEventLiveData =
                searchShopViewModel.getClickShopRecommendationItemTrackingEventLiveData().value

        clickShopRecommendationItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe null
    }

    @Test
    fun `Click Closed Shop`() {
        val shopItem = ShopViewModel.ShopItem(
                id = "12345",
                name = "Shop Name",
                status = SearchConstant.ShopStatus.KEY_SHOP_STATUS_CLOSED,
                applink = "tokopedia://shop/12345"
        ).also {
            it.position = 1
        }

        `When View Click Shop`(shopItem)

        `Then should post route to shop page with applink`(shopItem)
        `Then should post tracking click shop item`(shopItem)
        `Then should post tracking click not active shop item`(shopItem)
        `Then should NOT post tracking click shop recommendation item`()
    }

    private fun `Then should post tracking click not active shop item`(shopItem: ShopViewModel.ShopItem) {
        val clickNotActiveShopItemTrackingEventLiveData =
                searchShopViewModel.getClickNotActiveShopItemTrackingEventLiveData().value

        clickNotActiveShopItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe shopItem
    }

    @Test
    fun `Click Moderated Shop`() {
        val shopItem = ShopViewModel.ShopItem(
                id = "12345",
                name = "Shop Name",
                status = SearchConstant.ShopStatus.KEY_SHOP_STATUS_MODERATED,
                applink = "tokopedia://shop/12345"
        ).also {
            it.position = 1
        }

        `When View Click Shop`(shopItem)

        `Then should post route to shop page with applink`(shopItem)
        `Then should post tracking click shop item`(shopItem)
        `Then should post tracking click not active shop item`(shopItem)
        `Then should NOT post tracking click shop recommendation item`()
    }

    @Test
    fun `Click Inactive Shop`() {
        val shopItem = ShopViewModel.ShopItem(
                id = "12345",
                name = "Shop Name",
                status = SearchConstant.ShopStatus.KEY_SHOP_STATUS_INACTIVE,
                applink = "tokopedia://shop/12345"
        ).also {
            it.position = 1
        }


        `When View Click Shop`(shopItem)

        `Then should post route to shop page with applink`(shopItem)
        `Then should post tracking click shop item`(shopItem)
        `Then should post tracking click not active shop item`(shopItem)
        `Then should NOT post tracking click shop recommendation item`()
    }

    @Test
    fun `Click Shop Recommendation`() {
        val shopItem = ShopViewModel.ShopItem(
                id = "12345",
                name = "Shop Recommendation Name",
                status = 1,
                applink = "tokopedia://shop/12345",
                isRecommendation = true
        ).also {
            it.position = 1
        }

        `When View Click Shop`(shopItem)

        `Then should post route to shop page with applink`(shopItem)
        `Then should NOT post tracking click shop item`()
        `Then should NOT post tracking click not active shop item`()
        `Then should post tracking click shop recommendation item`(shopItem)
    }

    private fun `Then should NOT post tracking click shop item`() {
        val clickShopItemTrackingEventLiveData = searchShopViewModel.getClickShopItemTrackingEventLiveData().value

        clickShopItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe null
    }

    private fun `Then should post tracking click shop recommendation item`(shopItem: ShopViewModel.ShopItem) {
        val clickShopRecommendationItemTrackingEventLiveData =
                searchShopViewModel.getClickShopRecommendationItemTrackingEventLiveData().value

        clickShopRecommendationItemTrackingEventLiveData?.getContentIfNotHandled() shouldBe shopItem
    }
}