package com.tokopedia.centralizedpromo.analytic

import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_CLICK_PROMOTION_CARD
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_MVC_CLICK_CLOSE
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_MVC_CLICK_CREATE
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_MVC_IMPRESSION
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_MVC_PRODUCT_CLICK_BOTTOMSHEET
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_MVC_PRODUCT_IMPRESSION_BOTTOMSHEET
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_ON_GOING_CLICK
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_ON_GOING_IMPRESSION
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_BOTTOM_SHEET_CHECKBOX
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_BOTTOM_SHEET_CREATE_CAMPAIGN
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_BOTTOM_SHEET_PAYWALL
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CATEGORY_ADS_AND_PROMO
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CATEGORY_MVC_PRODUCT
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CLICK_CAMPAIGN_CARD
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CLICK_FILTER_ALL
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CLICK_FILTER_INCREASE_BUYER
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CLICK_FILTER_INCREASE_LOYALTY
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CLICK_FILTER_INCREASE_NEW_ORDER
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_IMPRESSION_BOTTOM_SHEET
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_IMPRESSION_CARD
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_NAME_CLICK
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_NAME_CLICK_PG
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_NAME_IMPRESSION
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_NAME_VIEW_PG_IRIS
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.ID_FILTER_ALL
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.ID_FILTER_INCREASE_BUYER
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.ID_FILTER_INCREASE_LOYALTY
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.ID_FILTER_INCREASE_NEW_ORDER
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.IMPRESSION_BOTTOM_SHEET_PAYWALL
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_BOTTOM_SHEET_CHECKBOX
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_BOTTOM_SHEET_CREATE_CAMPAIGN
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_BOTTOM_SHEET_PAYWALL
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_CLICK_CARD
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_FILTER_ALL
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_IMPRESSION_BOTTOM_SHEET
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_IMPRESSION_BOTTOM_SHEET_PAYWALL
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_IMPRESSION_CARD
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_INCREASE_BUYER
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_INCREASE_LOYALTY
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_INCREASE_NEW_ORDER
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_ON_GOING_CLICK
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_ON_GOING_IMPRESSION
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.EMPTY
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

// Doc : https://docs.google.com/spreadsheets/d/1d6OCqZyVOMsYrEChc-xwj1Ta_5G9bvCKIzOFw4kq4BA
object CentralizedPromoTracking {
    @Suppress("SameParameterValue")
    private fun createMap(
        event: String,
        category: String,
        action: String,
        label: String
    ): MutableMap<String, Any> {
        return mutableMapOf(
            TrackingConstant.EVENT to event,
            TrackingConstant.EVENT_CATEGORY to category,
            TrackingConstant.EVENT_ACTION to action,
            TrackingConstant.EVENT_LABEL to label,
        )
    }

    @Suppress("SameParameterValue")
    private fun createMap(
        event: String,
        category: String,
        action: String,
        label: String,
        trackerId: String = ""
    ): MutableMap<String, Any> {
        return mutableMapOf(
            TrackingConstant.EVENT to event,
            TrackingConstant.EVENT_CATEGORY to category,
            TrackingConstant.EVENT_ACTION to action,
            TrackingConstant.EVENT_LABEL to label,
            TrackingConstant.TRACKER_ID to trackerId
        )
    }

    fun sendImpressionOnGoingPromoStatus(widgetName: String) {
        val data = createMap(
            event = EVENT_NAME_VIEW_PG_IRIS,
            category = EVENT_CATEGORY_ADS_AND_PROMO,
            action = EVENT_ACTION_ON_GOING_IMPRESSION,
            label = widgetName,
            trackerId = TRACKER_ID_ON_GOING_IMPRESSION
        ).completeEventInfo()

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickOnGoingPromoStatus(widgetName: String) {
        val data = createMap(
            event = EVENT_NAME_VIEW_PG_IRIS,
            category = EVENT_CATEGORY_ADS_AND_PROMO,
            action = EVENT_ACTION_ON_GOING_CLICK,
            label = widgetName,
            trackerId = TRACKER_ID_ON_GOING_CLICK
        ).completeEventInfo()

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickOnGoingPromoFooter(widgetName: String) {
        val data = createMap(
            event = EVENT_NAME_VIEW_PG_IRIS,
            category = EVENT_CATEGORY_ADS_AND_PROMO,
            action = EVENT_ACTION_ON_GOING_CLICK,
            label = widgetName,
            trackerId = TRACKER_ID_ON_GOING_CLICK
        ).completeEventInfo()

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


    fun sendOpenScreenEvent(
        isLoggedIn: Boolean,
        userId: String
    ) {
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
        ).plus(
            mapOf(
                SCREEN_NAME to ADS_PROMOTION,
                CURRENT_SITE to TOKOPEDIA_SELLER,
                USER_ID to userId,
                BUSINESS_UNIT to PHYSICAL_GOODS
            )
        )
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

    fun sendFirstVoucherBottomSheetClick(
        userId: String,
        isClose: Boolean
    ) {
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
        ).plus(
            mapOf(
                SCREEN_NAME to ADS_PROMOTION,
                CURRENT_SITE to TOKOPEDIA_SELLER,
                USER_ID to userId
            )
        )
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

    fun sendClickFilter(filterId: String) {

        val trackerId = when (filterId) {
            ID_FILTER_ALL -> {
                TRACKER_ID_FILTER_ALL
            }
            ID_FILTER_INCREASE_BUYER -> {
                TRACKER_ID_INCREASE_BUYER
            }
            ID_FILTER_INCREASE_NEW_ORDER -> {
                TRACKER_ID_INCREASE_NEW_ORDER
            }
            ID_FILTER_INCREASE_LOYALTY -> {
                TRACKER_ID_INCREASE_LOYALTY
            }
            else -> {
                ""
            }
        }

        val eventName = when (filterId) {
            ID_FILTER_ALL -> {
                EVENT_CLICK_FILTER_ALL
            }
            ID_FILTER_INCREASE_BUYER -> {
                EVENT_CLICK_FILTER_INCREASE_BUYER
            }
            ID_FILTER_INCREASE_NEW_ORDER -> {
                EVENT_CLICK_FILTER_INCREASE_NEW_ORDER
            }
            ID_FILTER_INCREASE_LOYALTY -> {
                EVENT_CLICK_FILTER_INCREASE_LOYALTY
            }
            else -> {
                ""
            }
        }

        val data = createMap(
            event = EVENT_NAME_CLICK_PG,
            action = eventName,
            category = EVENT_CATEGORY_ADS_AND_PROMO,
            label = String.EMPTY,
            trackerId = trackerId
        ).completeEventInfo()

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendImpressionCard(featureName: String, tabFilterName: String) {

        val data = createMap(
            event = EVENT_NAME_VIEW_PG_IRIS,
            action = EVENT_IMPRESSION_CARD,
            category = EVENT_CATEGORY_ADS_AND_PROMO,
            label = "$tabFilterName - $featureName",
            trackerId = TRACKER_ID_IMPRESSION_CARD
        ).completeEventInfo()

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickCampaignCard(filterTabName: String, featureName: String) {

        val data = createMap(
            event = EVENT_NAME_CLICK_PG,
            action = EVENT_CLICK_CAMPAIGN_CARD,
            category = EVENT_CATEGORY_ADS_AND_PROMO,
            label = arrayOf(
                filterTabName,
                featureName
            ).joinToString(" - "),
            trackerId = TRACKER_ID_CLICK_CARD
        ).completeEventInfo()

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendImpressionBottomSheetPromo(filterTabName: String, featureName: String) {
        val data = createMap(
            event = EVENT_NAME_VIEW_PG_IRIS,
            action = EVENT_IMPRESSION_BOTTOM_SHEET,
            category = EVENT_CATEGORY_ADS_AND_PROMO,
            label = arrayOf(
                filterTabName,
                featureName
            ).joinToString(" - "),
            trackerId = TRACKER_ID_IMPRESSION_BOTTOM_SHEET
        ).completeEventInfo()

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickCheckboxBottomSheet(filterTabName: String, featureName: String) {
        val data = createMap(
            event = EVENT_NAME_CLICK_PG,
            action = EVENT_BOTTOM_SHEET_CHECKBOX,
            category = EVENT_CATEGORY_ADS_AND_PROMO,
            label = arrayOf(
                filterTabName,
                featureName
            ).joinToString(" - "),
            trackerId = TRACKER_ID_BOTTOM_SHEET_CHECKBOX
        ).completeEventInfo()

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickCreateCampaign(filterTabName: String, featureName: String) {
        val data = createMap(
            event = EVENT_NAME_CLICK_PG,
            action = EVENT_BOTTOM_SHEET_CREATE_CAMPAIGN,
            category = EVENT_CATEGORY_ADS_AND_PROMO,
            label = arrayOf(
                filterTabName,
                featureName
            ).joinToString(" - "),
            trackerId = TRACKER_ID_BOTTOM_SHEET_CREATE_CAMPAIGN
        ).completeEventInfo()

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickPaywall(filterTabName: String, featureName: String, ctaText: String) {
        val data = createMap(
            event = EVENT_NAME_CLICK_PG,
            action = EVENT_BOTTOM_SHEET_PAYWALL,
            category = EVENT_CATEGORY_ADS_AND_PROMO,
            label = arrayOf(
                filterTabName,
                featureName,
                ctaText
            ).joinToString(" - "),
            trackerId = TRACKER_ID_BOTTOM_SHEET_PAYWALL
        ).completeEventInfo()

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendImpressionBottomSheetPaywall(filterTabName: String, featureName: String, ctaText: String) {
        val data = createMap(
            event = EVENT_NAME_CLICK_PG,
            action = IMPRESSION_BOTTOM_SHEET_PAYWALL,
            category = EVENT_CATEGORY_ADS_AND_PROMO,
            label = arrayOf(
                filterTabName,
                featureName,
                ctaText
            ).joinToString(" - "),
            trackerId = TRACKER_ID_IMPRESSION_BOTTOM_SHEET_PAYWALL
        ).completeEventInfo()

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    private fun MutableMap<String, Any>.completeEventInfo(shopId: String): Map<String, Any> {
        return this.plus(
            mapOf(
                CURRENT_SITE to TOKOPEDIA_SELLER,
                SHOP_ID to shopId,
                BUSINESS_UNIT to PHYSICAL_GOODS
            )
        )
    }

    private fun MutableMap<String, Any>.completeEventInfo(): Map<String, Any> {
        return this.plus(
            mapOf(
                CURRENT_SITE to TOKOPEDIA_SELLER,
                BUSINESS_UNIT to PHYSICAL_GOODS
            )
        )
    }
}