package com.tokopedia.centralizedpromo.analytic

import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_CLICK_PROMOTION_CARD
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_FREE_SHIPPING_CLICK
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_FREE_SHIPPING_IMPRESSION
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_MVC_CLICK_CLOSE
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_MVC_CLICK_CREATE
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_MVC_IMPRESSION
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_MVC_PRODUCT_CLICK
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_MVC_PRODUCT_CLICK_BOTTOMSHEET
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_MVC_PRODUCT_IMPRESSION
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_MVC_PRODUCT_IMPRESSION_BOTTOMSHEET
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_ON_GOING_CLICK
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_ON_GOING_IMPRESSION
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_PROMO_CREATION_CLICK
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_PROMO_CREATION_IMPRESSION
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CATEGORY_ADS_AND_PROMO
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CATEGORY_MAIN_APP
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CATEGORY_MVC_PRODUCT
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CATEGORY_SELLER_APP
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_LABEL_CHARGE_PERIOD
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_LABEL_PM_ACTIVE
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_LABEL_PM_INACTIVE
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_LABEL_TRANSITION_PERIOD
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_NAME_CLICK
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_NAME_CLICK_PG
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_NAME_IMPRESSION
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_NAME_PM_CLICK
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_NAME_PM_IMPRESSION
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_NAME_VIEW_PG_IRIS
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.KEY_SHOP_TYPE
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.KEY_USER_ID
import com.tokopedia.config.GlobalConfig
import com.tokopedia.sellerhome.analytic.TrackingConstant
import com.tokopedia.sellerhome.analytic.TrackingConstant.ADS_PROMOTION
import com.tokopedia.sellerhome.analytic.TrackingConstant.BUSINESS_UNIT
import com.tokopedia.sellerhome.analytic.TrackingConstant.CURRENT_SITE
import com.tokopedia.sellerhome.analytic.TrackingConstant.EVENT
import com.tokopedia.sellerhome.analytic.TrackingConstant.IS_LOGGED_IN_STATUS
import com.tokopedia.sellerhome.analytic.TrackingConstant.OPEN_SCREEN
import com.tokopedia.sellerhome.analytic.TrackingConstant.PHYSICAL_GOODS
import com.tokopedia.sellerhome.analytic.TrackingConstant.SCREEN_NAME
import com.tokopedia.sellerhome.analytic.TrackingConstant.SHOP_ID
import com.tokopedia.sellerhome.analytic.TrackingConstant.TOKOPEDIA_SELLER
import com.tokopedia.sellerhome.analytic.TrackingConstant.USER_ID
import com.tokopedia.track.TrackApp
import com.tokopedia.user.session.UserSessionInterface

// Doc : https://docs.google.com/spreadsheets/d/1d6OCqZyVOMsYrEChc-xwj1Ta_5G9bvCKIzOFw4kq4BA
object CentralizedPromoTracking {
    @Suppress("SameParameterValue")
    private fun createMap(event: String, category: String, action: String, label: String): MutableMap<String, Any> {
        return mutableMapOf(
                TrackingConstant.EVENT to event,
                TrackingConstant.EVENT_CATEGORY to category,
                TrackingConstant.EVENT_ACTION to action,
                TrackingConstant.EVENT_LABEL to label
        )
    }

    fun sendImpressionOnGoingPromoStatus(widgetName: String, value: Int, state: String) {
        val data = createMap(
                event = EVENT_NAME_IMPRESSION,
                category = EVENT_CATEGORY_ADS_AND_PROMO,
                action = arrayOf(EVENT_ACTION_ON_GOING_IMPRESSION, widgetName, state).joinToString(" - "),
                label = value.toString()
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickOnGoingPromoStatus(widgetName: String, value: Int, state: String) {
        val data = createMap(
                event = EVENT_NAME_CLICK,
                category = EVENT_CATEGORY_ADS_AND_PROMO,
                action = arrayOf(EVENT_ACTION_ON_GOING_CLICK, widgetName, state).joinToString(" - "),
                label = value.toString()
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickOnGoingPromoFooter(widgetName: String, footerText: String) {
        val data = createMap(
                event = EVENT_NAME_CLICK,
                category = EVENT_CATEGORY_ADS_AND_PROMO,
                action = arrayOf(EVENT_ACTION_ON_GOING_CLICK, widgetName, footerText).joinToString(" - "),
                label = ""
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickProductCouponOngoingPromo(campaignName: String, shopId: String) {
        val data = createMap(
            event = EVENT_NAME_CLICK_PG,
            category = EVENT_CATEGORY_MVC_PRODUCT,
            action = EVENT_ACTION_CLICK_PROMOTION_CARD,
            label = campaignName
        ).completeEventInfo(shopId)

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendImpressionPromoCreation(widgetName: String) {
        val data = createMap(
                event = EVENT_NAME_IMPRESSION,
                category = EVENT_CATEGORY_ADS_AND_PROMO,
                action = arrayOf(EVENT_ACTION_PROMO_CREATION_IMPRESSION, widgetName).joinToString(" - "),
                label = ""
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendImpressionProductCouponPromoCreation(shopId: String) {
        val data = createMap(
            event = EVENT_NAME_VIEW_PG_IRIS,
            category = EVENT_CATEGORY_MVC_PRODUCT,
            action = EVENT_ACTION_MVC_PRODUCT_IMPRESSION,
            label = ""
        ).completeEventInfo(shopId)

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickPromoCreation(widgetName: String) {
        val data = createMap(
                event = EVENT_NAME_CLICK,
                category = EVENT_CATEGORY_ADS_AND_PROMO,
                action = arrayOf(EVENT_ACTION_PROMO_CREATION_CLICK, widgetName).joinToString(" - "),
                label = ""
        )

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickProductCouponPromoCreation(shopId: String) {
        val data = createMap(
            event = EVENT_NAME_CLICK_PG,
            category = EVENT_CATEGORY_MVC_PRODUCT,
            action = EVENT_ACTION_MVC_PRODUCT_CLICK,
            label = ""
        ).completeEventInfo(shopId)

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendOpenScreenEvent(isLoggedIn: Boolean,
                            userId: String) {
        val data = mapOf(
                EVENT to OPEN_SCREEN,
                SCREEN_NAME to ADS_PROMOTION,
                IS_LOGGED_IN_STATUS to isLoggedIn.toString(),
                CURRENT_SITE to TOKOPEDIA_SELLER,
                USER_ID to userId,
                BUSINESS_UNIT to PHYSICAL_GOODS
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendFirstVoucherBottomSheetImpression(userId: String) {
        val data = createMap(
                event = EVENT_NAME_IMPRESSION,
                category = EVENT_CATEGORY_ADS_AND_PROMO,
                action = EVENT_ACTION_MVC_IMPRESSION,
                label = ""
        ).plus(mapOf(
                SCREEN_NAME to ADS_PROMOTION,
                CURRENT_SITE to TOKOPEDIA_SELLER,
                USER_ID to userId,
                BUSINESS_UNIT to PHYSICAL_GOODS
        ))
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendFirstVoucherProductBottomSheetImpression(shopId: String) {
        val data = createMap(
            event = EVENT_NAME_VIEW_PG_IRIS,
            category = EVENT_CATEGORY_MVC_PRODUCT,
            action = EVENT_ACTION_MVC_PRODUCT_IMPRESSION_BOTTOMSHEET,
            label = ""
        ).completeEventInfo(shopId)

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendFirstVoucherBottomSheetClick(userId: String,
                                         isClose: Boolean) {
        val data = createMap(
                event = EVENT_NAME_CLICK,
                category = EVENT_CATEGORY_ADS_AND_PROMO,
                action =
                    if (isClose) {
                        EVENT_ACTION_MVC_CLICK_CLOSE
                    } else {
                        EVENT_ACTION_MVC_CLICK_CREATE
                    },
                label = ""
        ).plus(mapOf(
                SCREEN_NAME to ADS_PROMOTION,
                CURRENT_SITE to TOKOPEDIA_SELLER,
                USER_ID to userId
        ))
        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendFirstVoucherProductBottomSheetClick(shopId: String) {
        val data = createMap(
            event = EVENT_NAME_CLICK_PG,
            category = EVENT_CATEGORY_MVC_PRODUCT,
            action = EVENT_ACTION_MVC_PRODUCT_CLICK_BOTTOMSHEET,
            label = ""
        ).completeEventInfo(shopId)

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendImpressionFreeShipping(user: UserSessionInterface, transitionPeriod: Boolean) {
        val powerMerchant = user.isGoldMerchant
        val eventCategory = getEventCategory()
        val shopType = getShopType(powerMerchant)
        val noticeType = getNoticeType(transitionPeriod)

        val data = createMap(
            event = EVENT_NAME_PM_IMPRESSION,
            category = eventCategory,
            action = EVENT_ACTION_FREE_SHIPPING_IMPRESSION,
            label = "$shopType - $noticeType"
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

        val data = createMap(
            event = EVENT_NAME_PM_CLICK,
            category = eventCategory,
            action = EVENT_ACTION_FREE_SHIPPING_CLICK,
            label = "$shopType - $noticeType"
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

    private fun MutableMap<String, Any>.completeEventInfo(shopId: String): Map<String, Any> {
        return this.plus(mapOf(
            CURRENT_SITE to TOKOPEDIA_SELLER,
            SHOP_ID to shopId,
            BUSINESS_UNIT to PHYSICAL_GOODS
        ))
    }
}