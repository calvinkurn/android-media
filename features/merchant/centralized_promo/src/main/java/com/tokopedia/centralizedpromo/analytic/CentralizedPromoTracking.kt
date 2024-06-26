package com.tokopedia.centralizedpromo.analytic

import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_CLICK_PROMOTION_CARD
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_IMPRESSION_CARD_AOV
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_MVC_CLICK_CLOSE
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_MVC_CLICK_CREATE
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_MVC_IMPRESSION
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_MVC_PRODUCT_CLICK_BOTTOMSHEET
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_MVC_PRODUCT_IMPRESSION_BOTTOMSHEET
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_ON_GOING_CLICK
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_ON_GOING_IMPRESSION
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_WITHOUT_RECOMMENDATION
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_ACTION_WITH_RECOMMENDATION
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_BOTTOM_SHEET_CHECKBOX
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_BOTTOM_SHEET_CREATE_CAMPAIGN
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_BOTTOM_SHEET_PAYWALL
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CATEGORY_ADS_AND_PROMO
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CATEGORY_MVC_PRODUCT
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CLICK_CAMPAIGN_CARD
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CLICK_FILTER_ALL
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CLICK_FILTER_INCREASE_AVERAGE_ORDER_VALUE
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CLICK_FILTER_INCREASE_BUYER
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CLICK_FILTER_INCREASE_LOYALTY
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_CLICK_FILTER_INCREASE_NEW_ORDER
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_IMPRESSION_BOTTOM_SHEET
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_IMPRESSION_CARD
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_IMPRESSION_FILTER_INCREASE_AVERAGE_ORDER_VALUE
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_NAME_CLICK
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_NAME_CLICK_PG
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_NAME_IMPRESSION
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.EVENT_NAME_VIEW_PG_IRIS
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.ID_FILTER_ALL
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.ID_FILTER_INCREASE_AVERAGE_ORDER_VALUE
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.ID_FILTER_INCREASE_BUYER
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.ID_FILTER_INCREASE_LOYALTY
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.ID_FILTER_INCREASE_NEW_ORDER
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.IMPRESSION_BOTTOM_SHEET_PAYWALL
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_BOTTOM_SHEET_CHECKBOX
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_BOTTOM_SHEET_CREATE_CAMPAIGN
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_BOTTOM_SHEET_PAYWALL
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_CLICK_CARD
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_CLICK_INCREASE_AVERAGE_ORDER_VALUE
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_FILTER_ALL
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_IMPRESSION_BOTTOM_SHEET
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_IMPRESSION_BOTTOM_SHEET_PAYWALL
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_IMPRESSION_CARD
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_IMPRESSION_CARD_AOV
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_IMPRESSION_INCREASE_AVERAGE_ORDER_VALUE
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_INCREASE_BUYER
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_INCREASE_LOYALTY
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_INCREASE_NEW_ORDER
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_ON_GOING_CLICK
import com.tokopedia.centralizedpromo.analytic.CentralizedPromoConstant.TRACKER_ID_ON_GOING_IMPRESSION
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.track.TrackApp
import com.tokopedia.track.constant.TrackerConstant

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
            TrackerConstant.EVENT to event,
            TrackerConstant.EVENT_CATEGORY to category,
            TrackerConstant.EVENT_ACTION to action,
            TrackerConstant.EVENT_LABEL to label
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
            TrackerConstant.EVENT to event,
            TrackerConstant.EVENT_CATEGORY to category,
            TrackerConstant.EVENT_ACTION to action,
            TrackerConstant.EVENT_LABEL to label,
            TrackerConstant.TRACKER_ID to trackerId
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
            event = EVENT_NAME_CLICK_PG,
            category = EVENT_CATEGORY_ADS_AND_PROMO,
            action = EVENT_ACTION_ON_GOING_CLICK,
            label = widgetName,
            trackerId = TRACKER_ID_ON_GOING_CLICK
        ).completeEventInfo()

        TrackApp.getInstance().gtm.sendGeneralEvent(data)
    }

    fun sendClickOnGoingPromoFooter(widgetName: String) {
        val data = createMap(
            event = EVENT_NAME_CLICK_PG,
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
            TrackerConstant.EVENT to CentralizedPromoConstant.OPEN_SCREEN,
            TrackerConstant.SCREEN_NAME to CentralizedPromoConstant.ADS_PROMOTION,
            CentralizedPromoConstant.IS_LOGGED_IN_STATUS to isLoggedIn.toString(),
            TrackerConstant.CURRENT_SITE to CentralizedPromoConstant.TOKOPEDIA_SELLER,
            TrackerConstant.USERID to userId,
            TrackerConstant.BUSINESS_UNIT to CentralizedPromoConstant.PHYSICAL_GOODS
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
                TrackerConstant.SCREEN_NAME to CentralizedPromoConstant.ADS_PROMOTION,
                TrackerConstant.CURRENT_SITE to CentralizedPromoConstant.TOKOPEDIA_SELLER,
                TrackerConstant.USERID to userId,
                TrackerConstant.BUSINESS_UNIT to CentralizedPromoConstant.PHYSICAL_GOODS
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
                TrackerConstant.SCREEN_NAME to CentralizedPromoConstant.ADS_PROMOTION,
                TrackerConstant.CURRENT_SITE to CentralizedPromoConstant.TOKOPEDIA_SELLER,
                TrackerConstant.USERID to userId
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

    fun sendImpressionAovFilter(isSelected: Boolean) {
        val eventLabel =
            if (isSelected) {
                EVENT_ACTION_WITH_RECOMMENDATION
            } else {
                EVENT_ACTION_WITHOUT_RECOMMENDATION
            }
        val data = createMap(
            event = EVENT_NAME_VIEW_PG_IRIS,
            action = EVENT_IMPRESSION_FILTER_INCREASE_AVERAGE_ORDER_VALUE,
            category = EVENT_CATEGORY_ADS_AND_PROMO,
            label = eventLabel,
            trackerId = TRACKER_ID_IMPRESSION_INCREASE_AVERAGE_ORDER_VALUE
        ).completeEventInfo()

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
            ID_FILTER_INCREASE_AVERAGE_ORDER_VALUE -> {
                TRACKER_ID_CLICK_INCREASE_AVERAGE_ORDER_VALUE
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
            ID_FILTER_INCREASE_AVERAGE_ORDER_VALUE -> {
                EVENT_CLICK_FILTER_INCREASE_AVERAGE_ORDER_VALUE
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

    fun sendImpressionAovCard(featureName: String) {
        val data = createMap(
            event = EVENT_NAME_VIEW_PG_IRIS,
            action = EVENT_ACTION_IMPRESSION_CARD_AOV,
            category = EVENT_CATEGORY_ADS_AND_PROMO,
            label = "$featureName - 1",
            trackerId = TRACKER_ID_IMPRESSION_CARD_AOV
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
                TrackerConstant.CURRENT_SITE to CentralizedPromoConstant.TOKOPEDIA_SELLER,
                TrackerConstant.SHOP_ID to shopId,
                TrackerConstant.BUSINESS_UNIT to CentralizedPromoConstant.PHYSICAL_GOODS
            )
        )
    }

    private fun MutableMap<String, Any>.completeEventInfo(): Map<String, Any> {
        return this.plus(
            mapOf(
                TrackerConstant.CURRENT_SITE to CentralizedPromoConstant.TOKOPEDIA_SELLER,
                TrackerConstant.BUSINESS_UNIT to CentralizedPromoConstant.PHYSICAL_GOODS
            )
        )
    }
}
