package com.tokopedia.power_merchant.subscribe.tracking

import com.tokopedia.config.GlobalConfig
import com.tokopedia.power_merchant.subscribe.view_old.model.PowerMerchantFreeShippingStatus
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.user.session.UserSessionInterface

object PowerMerchantFreeShippingTracker {

    // POWER MERCHANT
    private const val EVENT_CATEGORY_SELLER_APP = "tokopedia seller app"
    private const val EVENT_CATEGORY_MAIN_APP = "tokopedia main app"

    private const val EVENT_NAME_PM_IMPRESSION = "viewPowerMerchantIris"
    private const val EVENT_NAME_PM_CLICK = "clickPowerMerchant"

    private const val EVENT_NAME_PM_FREE_SHIPPING_IMPRESSION = "viewBebasOngkirIris"
    private const val EVENT_NAME_PM_FREE_SHIPPING_CLICK = "clickBebasOngkir"

    private const val EVENT_ACTION_FREE_SHIPPING_IMPRESSION = "impression on BBO Banner"
    private const val EVENT_ACTION_FREE_SHIPPING_CLICK = "click on BBO Banner"
    private const val EVENT_ACTION_SUCCESS_PM_POP_UP = "pop up - sucess PM notifier"
    private const val EVENT_ACTION_SUCCESS_PM_CLICK_LEARN_MORE = "click learn more BBO - sucess PM notifier"
    private const val EVENT_ACTION_IMPRESSION_BBO_BANNER = "impression on BBO Banner - PM Page"
    private const val EVENT_ACTION_CLICK_BBO_BANNER = "click on BBO Banner - PM Page"

    private const val EVENT_LABEL_PM_ACTIVE = "PM Active"
    private const val EVENT_LABEL_PM_INACTIVE = "PM Inactive"
    private const val EVENT_LABEL_TRANSITION_PERIOD = "Transition Period"
    private const val EVENT_LABEL_ELIGIBLE_BBO = "eligible BBO"
    private const val EVENT_LABEL_BBO_ACTIVE = "BBO Active"
    private const val EVENT_LABEL_BBO_INACTIVE = "BBO Inactive"
    private const val EVENT_LABEL_BBO_ELIGIBLE = "Eligible Bebas Ongkir"
    private const val EVENT_LABEL_BBO_NOT_ELIGIBLE = "not Eligible Bebas Ongkir"

    // KEY
    private const val KEY_USER_ID = "user_id"
    private const val KEY_SHOP_ID = "shop_id"
    private const val KEY_SHOP_TYPE = "shop_type"

    fun sendImpressionFreeShipping(
        user: UserSessionInterface,
        freeShippingStatus: PowerMerchantFreeShippingStatus
    ) {
        val powerMerchant = freeShippingStatus.isPowerMerchantActive

        val eventCategory = getEventCategory()
        val eventName = getImpressionEventName(freeShippingStatus)
        val eventAction = getImpressionAction(freeShippingStatus)

        val shopType = getShopType(powerMerchant)
        val noticeType = getNoticeType(powerMerchant, freeShippingStatus)

        val data = TrackAppUtils.gtmData(
            eventName,
            eventCategory,
            eventAction,
            "$shopType - $noticeType"
        )

        data[KEY_USER_ID] = user.userId
        data[KEY_SHOP_ID] = user.shopId
        data[KEY_SHOP_TYPE] = shopType

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickFreeShipping(
        user: UserSessionInterface,
        freeShippingStatus: PowerMerchantFreeShippingStatus
    ) {
        val powerMerchant = freeShippingStatus.isPowerMerchantActive

        val eventCategory = getEventCategory()
        val eventName = getClickEventName(freeShippingStatus)

        val shopType = getShopType(powerMerchant)
        val noticeType = getNoticeType(powerMerchant, freeShippingStatus)

        val data = TrackAppUtils.gtmData(
            eventName,
            eventCategory,
            getClickAction(freeShippingStatus),
            "$shopType - $noticeType"
        )

        data[KEY_USER_ID] = user.userId
        data[KEY_SHOP_ID] = user.shopId
        data[KEY_SHOP_TYPE] = shopType

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun eventFreeShippingSuccessBottomSheet(
        user: UserSessionInterface,
        freeShippingStatus: PowerMerchantFreeShippingStatus
    ) {
        val powerMerchant = freeShippingStatus.isPowerMerchantActive
        val eventCategory = getEventCategory()
        val shopType = getShopType(powerMerchant)

        val data = TrackAppUtils.gtmData(
            EVENT_NAME_PM_FREE_SHIPPING_IMPRESSION,
            eventCategory,
            EVENT_ACTION_SUCCESS_PM_POP_UP,
            EVENT_LABEL_ELIGIBLE_BBO
        )

        data[KEY_USER_ID] = user.userId
        data[KEY_SHOP_ID] = user.shopId
        data[KEY_SHOP_TYPE] = shopType

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendSuccessBottomSheetClickLearnMore(
        user: UserSessionInterface,
        freeShippingStatus: PowerMerchantFreeShippingStatus
    ) {
        val powerMerchant = freeShippingStatus.isPowerMerchantActive
        val eventCategory = getEventCategory()
        val shopType = getShopType(powerMerchant)

        val data = TrackAppUtils.gtmData(
            EVENT_NAME_PM_FREE_SHIPPING_CLICK,
            eventCategory,
            EVENT_ACTION_SUCCESS_PM_CLICK_LEARN_MORE,
            EVENT_LABEL_ELIGIBLE_BBO
        )

        data[KEY_USER_ID] = user.userId
        data[KEY_SHOP_ID] = user.shopId
        data[KEY_SHOP_TYPE] = shopType

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    private fun getNoticeType(
        powerMerchant: Boolean,
        freeShippingStatus: PowerMerchantFreeShippingStatus
    ): String {
        val isActive = freeShippingStatus.isActive
        val isEligible = freeShippingStatus.isEligible
        val transitionPeriod = freeShippingStatus.isTransitionPeriod

        return when {
            transitionPeriod -> EVENT_LABEL_TRANSITION_PERIOD
            !powerMerchant -> EVENT_LABEL_BBO_INACTIVE
            isActive -> EVENT_LABEL_BBO_ACTIVE
            isEligible -> EVENT_LABEL_BBO_ELIGIBLE
            else -> EVENT_LABEL_BBO_NOT_ELIGIBLE
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

    private fun getImpressionEventName(freeShippingStatus: PowerMerchantFreeShippingStatus): String  {
        return if(freeShippingStatus.isTransitionPeriod) {
            EVENT_NAME_PM_IMPRESSION
        } else {
            EVENT_NAME_PM_FREE_SHIPPING_IMPRESSION
        }
    }

    private fun getClickEventName(freeShippingStatus: PowerMerchantFreeShippingStatus): String  {
        return if(freeShippingStatus.isTransitionPeriod) {
            EVENT_NAME_PM_CLICK
        } else {
            EVENT_NAME_PM_FREE_SHIPPING_CLICK
        }
    }

    private fun getImpressionAction(freeShippingStatus: PowerMerchantFreeShippingStatus): String  {
        return if(freeShippingStatus.isTransitionPeriod) {
            EVENT_ACTION_FREE_SHIPPING_IMPRESSION
        } else {
            EVENT_ACTION_IMPRESSION_BBO_BANNER
        }
    }

    private fun getClickAction(freeShippingStatus: PowerMerchantFreeShippingStatus): String  {
        return if(freeShippingStatus.isTransitionPeriod) {
            EVENT_ACTION_FREE_SHIPPING_CLICK
        } else {
            EVENT_ACTION_CLICK_BBO_BANNER
        }
    }
}