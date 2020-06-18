package com.tokopedia.power_merchant.subscribe.tracking

import com.tokopedia.config.GlobalConfig
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface

object PowerMerchantFreeShippingTracker {

    // POWER MERCHANT
    private const val EVENT_CATEGORY_SELLER_APP = "tokopedia seller app"
    private const val EVENT_CATEGORY_MAIN_APP = "tokopedia main app"

    private const val EVENT_NAME_PM_IMPRESSION = "viewPowerMerchantIris"
    private const val EVENT_NAME_PM_CLICK = "clickPowerMerchant"

    private const val EVENT_ACTION_FREE_SHIPPING_IMPRESSION = "impression on BBO Banner"
    private const val EVENT_ACTION_FREE_SHIPPING_CLICK = "impression on BBO Banner"

    private const val EVENT_LABEL_PM_ACTIVE = "PM Active"
    private const val EVENT_LABEL_PM_INACTIVE = "PM Inactive"
    private const val EVENT_LABEL_TRANSITION_PERIOD = "Transition Period"
    private const val EVENT_LABEL_CHARGE_PERIOD = "Charge Period"

    // KEY
    private const val KEY_USER_ID = "user_id"
    private const val KEY_SHOP_TYPE = "shop_type"

    fun sendImpressionFreeShipping(user: UserSessionInterface, transitionPeriod: Boolean) {
        val powerMerchant = user.isGoldMerchant
        val eventCategory = getEventCategory()
        val shopType = getShopType(powerMerchant)
        val noticeType = getNoticeType(transitionPeriod)

        val data = TrackAppUtils.gtmData(
            EVENT_NAME_PM_IMPRESSION,
            eventCategory,
            EVENT_ACTION_FREE_SHIPPING_IMPRESSION,
            "$shopType - $noticeType"
        )

        data[KEY_USER_ID] = user.userId
        data[KEY_SHOP_TYPE] = shopType

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickFreeShipping(user: UserSessionInterface, transitionPeriod: Boolean) {
        val powerMerchant = user.isGoldMerchant
        val eventCategory = getEventCategory()
        val shopType = getShopType(powerMerchant)
        val noticeType = getNoticeType(transitionPeriod)

        val data = TrackAppUtils.gtmData(
            EVENT_NAME_PM_CLICK,
            eventCategory,
            EVENT_ACTION_FREE_SHIPPING_CLICK,
            "$shopType - $noticeType"
        )

        data[KEY_USER_ID] = user.userId
        data[KEY_SHOP_TYPE] = shopType

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    private fun getNoticeType(transitionPeriod: Boolean): String {
        return if (transitionPeriod) {
            EVENT_LABEL_TRANSITION_PERIOD
        } else {
            EVENT_LABEL_CHARGE_PERIOD
        }
    }

    private fun getShopType(powerMerchant: Boolean): String {
        return if (powerMerchant) {
            EVENT_LABEL_PM_ACTIVE
        } else {
            EVENT_LABEL_PM_INACTIVE
        }
    }

    private fun getEventCategory():  String {
        return if(GlobalConfig.isSellerApp()) {
            EVENT_CATEGORY_SELLER_APP
        } else {
            EVENT_CATEGORY_MAIN_APP
        }
    }
}