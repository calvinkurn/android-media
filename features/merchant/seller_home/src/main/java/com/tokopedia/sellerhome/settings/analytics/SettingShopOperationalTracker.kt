package com.tokopedia.sellerhome.settings.analytics

import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import java.util.*

class SettingShopOperationalTracker(private val analytics: Analytics) {

    companion object {
        private const val EVENT_SCREEN_NAME = "screenName"
        private const val EVENT_BUSINESS_UNIT = "businessUnit"
        private const val EVENT_CURRENT_SITE = "currentSite"

        private const val EVENT_SCREEN_NAME_SELLER_NAVIGATION = "Seller Navigation"
        private const val EVENT_CLICK_OTHER_WIDGET = "clickOtherWidget"
        private const val EVENT_CATEGORY_OTHERS_TAB = "others tab"
        private const val EVENT_CLICK_SHOP_OPERATIONAL_HOUR = "click shop operational hour"
        private const val EVENT_BUSINESS_UNIT_PHYSICAL_GOODS = "physical goods"
        private const val EVENT_CURRENT_SITE_MARKETPLACE = "tokopediamarketplace"
    }

    fun trackClickShopOperationalHour(shopOperationalStatus: String) {
        val event = TrackAppUtils.gtmData(
            EVENT_CLICK_OTHER_WIDGET,
            EVENT_CATEGORY_OTHERS_TAB,
            EVENT_CLICK_SHOP_OPERATIONAL_HOUR,
            shopOperationalStatus.toLowerCase(Locale.getDefault())
        )
        event[EVENT_SCREEN_NAME] = EVENT_SCREEN_NAME_SELLER_NAVIGATION
        event[EVENT_BUSINESS_UNIT] = EVENT_BUSINESS_UNIT_PHYSICAL_GOODS
        event[EVENT_CURRENT_SITE] = EVENT_CURRENT_SITE_MARKETPLACE
        analytics.sendGeneralEvent(event)
    }
}