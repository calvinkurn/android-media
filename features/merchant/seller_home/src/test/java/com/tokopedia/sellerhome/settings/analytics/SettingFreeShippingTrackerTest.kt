package com.tokopedia.sellerhome.settings.analytics

import org.junit.Test

class SettingFreeShippingTrackerTest : SettingFreeShippingTrackerTestFixture() {

    @Test
    fun `when trackFreeShippingImpression should send free shipping impression data layer`() {
        val userId = "1200"
        val shopId = "1000"
        val isGoldMerchant = true

        onGetUserId_thenReturn(userId)
        onGetShopId_thenReturn(shopId)
        onGetIsGoldMerchant_thenReturn(isGoldMerchant)

        tracker.trackFreeShippingImpression()

        val expectedEvent = mapOf(
            EVENT to EVENT_NAME_PM_FREE_SHIPPING_IMPRESSION,
            EVENT_CATEGORY to EVENT_CATEGORY_SELLER_APP,
            EVENT_ACTION to EVENT_ACTION_IMPRESSION_BBO_MENU,
            EVENT_LABEL to "",
            KEY_USER_ID to userId,
            KEY_SHOP_ID to shopId,
            KEY_SHOP_TYPE to EVENT_LABEL_PM_ACTIVE
        )

        verifySendGeneralEvent(expectedEvent)
    }

    @Test
    fun `when trackFreeShippingClick should send free shipping click data layer`() {
        val userId = "1000"
        val shopId = "1100"
        val isGoldMerchant = false

        onGetUserId_thenReturn(userId)
        onGetShopId_thenReturn(shopId)
        onGetIsGoldMerchant_thenReturn(isGoldMerchant)

        tracker.trackFreeShippingClick()

        val expectedEvent = mapOf(
            EVENT to EVENT_NAME_PM_FREE_SHIPPING_CLICK,
            EVENT_CATEGORY to EVENT_CATEGORY_SELLER_APP,
            EVENT_ACTION to EVENT_ACTION_CLICK_BBO_MENU,
            EVENT_LABEL to "",
            KEY_USER_ID to userId,
            KEY_SHOP_ID to shopId,
            KEY_SHOP_TYPE to EVENT_LABEL_PM_INACTIVE
        )

        verifySendGeneralEvent(expectedEvent)
    }

    @Test
    fun `when trackFreeShippingDetailClick should send free shipping click detail data layer`() {
        val userId = "1000"
        val shopId = "1100"
        val isGoldMerchant = false

        onGetUserId_thenReturn(userId)
        onGetShopId_thenReturn(shopId)
        onGetIsGoldMerchant_thenReturn(isGoldMerchant)

        tracker.trackFreeShippingDetailClick()

        val expectedEvent = mapOf(
            EVENT to EVENT_NAME_PM_FREE_SHIPPING_CLICK,
            EVENT_CATEGORY to EVENT_CATEGORY_SELLER_APP,
            EVENT_ACTION to EVENT_ACTION_CLICK_DETAIL_BBO_MENU,
            EVENT_LABEL to "",
            KEY_USER_ID to userId,
            KEY_SHOP_ID to shopId,
            KEY_SHOP_TYPE to EVENT_LABEL_PM_INACTIVE
        )

        verifySendGeneralEvent(expectedEvent)
    }

    @Test
    fun `given is gold merchant true when track event should send event with shopType PM active`() {
        testSendEventShopType(
            isGoldMerchant = true,
            expectedShopType = EVENT_LABEL_PM_ACTIVE
        )
    }

    @Test
    fun `given is gold merchant false when track event should send event with shopType PM inactive`() {
        testSendEventShopType(
            isGoldMerchant = false,
            expectedShopType = EVENT_LABEL_PM_INACTIVE
        )
    }

    private fun testSendEventShopType(isGoldMerchant: Boolean, expectedShopType: String) {
        val userId = "1200"
        val shopId = "1000"

        onGetUserId_thenReturn(userId)
        onGetShopId_thenReturn(shopId)
        onGetIsGoldMerchant_thenReturn(isGoldMerchant)

        tracker.trackFreeShippingImpression()

        val expectedEvent = mapOf(
            EVENT to EVENT_NAME_PM_FREE_SHIPPING_IMPRESSION,
            EVENT_CATEGORY to EVENT_CATEGORY_SELLER_APP,
            EVENT_ACTION to EVENT_ACTION_IMPRESSION_BBO_MENU,
            EVENT_LABEL to "",
            KEY_USER_ID to userId,
            KEY_SHOP_ID to shopId,
            KEY_SHOP_TYPE to expectedShopType
        )

        verifySendGeneralEvent(expectedEvent)
    }

    companion object {
        // CATEGORY
        private const val EVENT_CATEGORY_SELLER_APP = "tokopedia seller app"

        // EVENT NAME
        private const val EVENT_NAME_PM_FREE_SHIPPING_IMPRESSION = "viewBebasOngkirIris"
        private const val EVENT_NAME_PM_FREE_SHIPPING_CLICK = "clickBebasOngkir"

        // ACTION
        private const val EVENT_ACTION_IMPRESSION_BBO_MENU = "impression BBO menu"
        private const val EVENT_ACTION_CLICK_BBO_MENU = "click BBO menu"
        private const val EVENT_ACTION_CLICK_DETAIL_BBO_MENU = "click detail BBO - popup menu"

        // LABEL
        private const val EVENT_LABEL_PM_ACTIVE = "PM Active"
        private const val EVENT_LABEL_PM_INACTIVE = "PM Inactive"

        // KEY
        private const val KEY_USER_ID = "shop_id"
        private const val KEY_SHOP_ID = "shop_id"
        private const val KEY_SHOP_TYPE = "shop_type"
        private const val EVENT = "event"
        private const val EVENT_CATEGORY = "eventCategory"
        private const val EVENT_ACTION = "eventAction"
        private const val EVENT_LABEL = "eventLabel"
    }
}