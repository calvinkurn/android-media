package com.tokopedia.sellerhome.settings.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface

object SettingFreeShippingTracker {

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

    fun trackFreeShippingImpression(user: UserSessionInterface) {
        val powerMerchant = user.isGoldMerchant
        val shopType = getShopType(powerMerchant)

        val event = TrackAppUtils.gtmData(
            EVENT_NAME_PM_FREE_SHIPPING_IMPRESSION,
            EVENT_CATEGORY_SELLER_APP,
            EVENT_ACTION_IMPRESSION_BBO_MENU,
            ""
        )

        event[KEY_USER_ID] = user.userId
        event[KEY_SHOP_ID] = user.shopId
        event[KEY_SHOP_TYPE] = shopType

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun trackFreeShippingClick(user: UserSessionInterface) {
        val powerMerchant = user.isGoldMerchant
        val shopType = getShopType(powerMerchant)

        val event = TrackAppUtils.gtmData(
            EVENT_NAME_PM_FREE_SHIPPING_CLICK,
            EVENT_CATEGORY_SELLER_APP,
            EVENT_ACTION_CLICK_BBO_MENU,
            ""
        )

        event[KEY_USER_ID] = user.userId
        event[KEY_SHOP_ID] = user.shopId
        event[KEY_SHOP_TYPE] = shopType

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun trackFreeShippingDetailClick(user: UserSessionInterface) {
        val powerMerchant = user.isGoldMerchant
        val shopType = getShopType(powerMerchant)

        val event = TrackAppUtils.gtmData(
            EVENT_NAME_PM_FREE_SHIPPING_CLICK,
            EVENT_CATEGORY_SELLER_APP,
            EVENT_ACTION_CLICK_DETAIL_BBO_MENU,
            ""
        )

        event[KEY_USER_ID] = user.userId
        event[KEY_SHOP_ID] = user.shopId
        event[KEY_SHOP_TYPE] = shopType

        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    private fun getShopType(powerMerchant: Boolean): String {
        return if (powerMerchant) {
            EVENT_LABEL_PM_ACTIVE
        } else {
            EVENT_LABEL_PM_INACTIVE
        }
    }
}