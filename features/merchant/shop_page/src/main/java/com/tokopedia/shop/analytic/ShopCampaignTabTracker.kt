package com.tokopedia.shop.analytic

import android.os.Bundle
import android.os.Parcelable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.BUSINESS_UNIT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_PG
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CREATIVE_NAME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CREATIVE_SLOT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CURRENT_SITE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_40
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_ACTION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_CATEGORY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_LABEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.Event.VIEW_PG_IRIS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_BANNER_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_CAMPAIGN_PLAY_WIDGET_ITEM
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_CTA_SLIDER_BANNER_HIGHLIGHT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_CTA_VOUCHER_SLIDER_ITEM_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_PRODUCT_HIGHLIGHT_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_PRODUCT_SLIDER_BANNER_HIGHLIGHT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_REMIND_ME_MAIN_BANNER_CAMPAIGN_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_SEE_MORE_VOUCHER_SLIDER_ITEM_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_TITLE_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_VOUCHER_SLIDER_ITEM_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.IMPRESSION_BANNER_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.IMPRESSION_CAMPAIGN_PLAY_WIDGET_ITEM
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.IMPRESSION_MAIN_BANNER_CAMPAIGN_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.IMPRESSION_PRODUCT_HIGHLIGHT_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.IMPRESSION_PRODUCT_SLIDER_BANNER_HIGHLIGHT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.IMPRESSION_TITLE_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.IMPRESSION_VOUCHER_SLIDER_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.HIGHLIGHT_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.HOME_ANCHOR
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.INDEX
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEMS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_BRAND
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_CATEGORY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_LIST
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_NAME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_VARIANT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PHYSICAL_GOODS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PLAY_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRICE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SELECT_CONTENT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOPPAGE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_BUYER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TRACKER_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_BANNER_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_CTA_VOUCHER_SLIDER_ITEM
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_EXPLORE_SHOP_SLIDER_BANNER_HIGHLIGHT_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_PLAY_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_PRODUCT_HIGHLIGHT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_PRODUCT_SLIDER_BANNER_HIGHLIGHT_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_REMIND_ME_MAIN_BANNER_CAMPAIGN_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_SEE_MORE_VOUCHER_SLIDER_ITEM
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_TITLE_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_VOUCHER_SLIDER_ITEM
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_BANNER_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_MAIN_BANNER_CAMPAIGN_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_PLAY_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_PRODUCT_HIGHLIGHT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_SLIDER_BANNER_HIGHLIGHT_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_TITLE_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_VOUCHER_SLIDER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.USER_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VIEW_ITEM
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VIEW_ITEM_LIST
import com.tokopedia.shop.analytic.model.ClickSeeMoreVoucherSliderItemTrackerModel
import com.tokopedia.shop.analytic.model.ShopCampaignBannerTimerTrackerDataModel
import com.tokopedia.shop.analytic.model.ShopCampaignClickCtaSliderBannerHighlightTrackerDataModel
import com.tokopedia.shop.analytic.model.ShopCampaignHeaderTitleTrackerDataModel
import com.tokopedia.shop.analytic.model.ShopCampaignPlayWidgetItemTrackerDataModel
import com.tokopedia.shop.analytic.model.ShopCampaignProductSliderBannerHighlightTrackerDataModel
import com.tokopedia.shop.analytic.model.ShopCampaignWidgetDisplayTrackerDataModel
import com.tokopedia.shop.analytic.model.ShopCampaignWidgetProductHighlightTrackerModel
import com.tokopedia.shop.analytic.model.ShopCampaignWidgetVoucherSliderItemClickCtaTrackerModel
import com.tokopedia.shop.analytic.model.ShopCampaignWidgetVoucherSliderItemClickTrackerModel
import com.tokopedia.shop.analytic.model.ShopCampaignWidgetVoucherSliderTrackerModel
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.track.TrackApp
import javax.inject.Inject

/*
Data Layer Docs:

Exclusive launch campaign tab revamp
https://mynakama.tokopedia.com/datatracker/requestdetail/view/4008

 */

class ShopCampaignTabTracker @Inject constructor() {
    fun sendImpressionShopBannerTimerCampaignTracker(
        trackerDataModel: ShopCampaignBannerTimerTrackerDataModel
    ) {
        val eventLabelValue = ShopUtil.joinDash(
            trackerDataModel.campaignId,
            trackerDataModel.widgetId
        )
        val eventMap = mapOf(
            EVENT to VIEW_PG_IRIS,
            EVENT_ACTION to IMPRESSION_MAIN_BANNER_CAMPAIGN_TAB,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to eventLabelValue,
            TRACKER_ID to TRACKER_ID_IMPRESSION_MAIN_BANNER_CAMPAIGN_TAB,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to trackerDataModel.shopId,
            USER_ID to trackerDataModel.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun sendClickRemindMeShopCampaignBannerTimerTracker(
        trackerDataModel: ShopCampaignBannerTimerTrackerDataModel
    ) {
        val eventLabelValue = ShopUtil.joinDash(
            trackerDataModel.campaignId,
            trackerDataModel.widgetId
        )
        val eventMap = mapOf(
            EVENT to CLICK_PG,
            EVENT_ACTION to CLICK_REMIND_ME_MAIN_BANNER_CAMPAIGN_TAB,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to eventLabelValue,
            TRACKER_ID to TRACKER_ID_CLICK_REMIND_ME_MAIN_BANNER_CAMPAIGN_TAB,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to trackerDataModel.shopId,
            USER_ID to trackerDataModel.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun impressionWidgetHeaderTitle(
        trackerDataModel: ShopCampaignHeaderTitleTrackerDataModel
    ) {
        val eventMap = mapOf(
            EVENT to VIEW_PG_IRIS,
            EVENT_ACTION to IMPRESSION_TITLE_WIDGET,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to trackerDataModel.widgetId,
            TRACKER_ID to TRACKER_ID_IMPRESSION_TITLE_WIDGET,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to trackerDataModel.shopId,
            USER_ID to trackerDataModel.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun clickCtaHeaderTitle(
        trackerDataModel: ShopCampaignHeaderTitleTrackerDataModel
    ) {
        val eventMap = mapOf(
            EVENT to CLICK_PG,
            EVENT_ACTION to CLICK_TITLE_WIDGET,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to trackerDataModel.widgetId,
            TRACKER_ID to TRACKER_ID_CLICK_TITLE_WIDGET,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to trackerDataModel.shopId,
            USER_ID to trackerDataModel.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun impressionProductHighlightCarouselProductItem(
        trackerDataModel: ShopCampaignWidgetProductHighlightTrackerModel
    ) {
        val eventLabelValue = ShopUtil.joinDash(
            trackerDataModel.campaignId,
            trackerDataModel.widgetId
        )
        val itemListValue = ShopUtil.joinDash(SHOPPAGE, HIGHLIGHT_WIDGET, trackerDataModel.tabName)
        val eventBundle = Bundle().apply {
            putString(EVENT, VIEW_ITEM_LIST)
            putString(EVENT_ACTION, IMPRESSION_PRODUCT_HIGHLIGHT_WIDGET)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, eventLabelValue)
            putString(TRACKER_ID, TRACKER_ID_IMPRESSION_PRODUCT_HIGHLIGHT)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(ITEM_LIST, itemListValue)
            putParcelableArrayList(
                ITEMS,
                arrayListOf(
                    createProductHighlightItemMap(
                        trackerDataModel,
                        itemListValue
                    )
                )
            )
            putString(PRODUCT_ID, trackerDataModel.productId)
            putString(SHOP_ID, trackerDataModel.shopId)
            putString(USER_ID, trackerDataModel.userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM_LIST, eventBundle)
    }

    fun clickProductHighlightCarouselProductItem(
        trackerDataModel: ShopCampaignWidgetProductHighlightTrackerModel
    ) {
        val eventLabelValue = ShopUtil.joinDash(
            trackerDataModel.campaignId,
            trackerDataModel.widgetId
        )
        val itemListValue = ShopUtil.joinDash(SHOPPAGE, HIGHLIGHT_WIDGET, trackerDataModel.tabName)
        val eventBundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_ACTION, CLICK_PRODUCT_HIGHLIGHT_WIDGET)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, eventLabelValue)
            putString(TRACKER_ID, TRACKER_ID_CLICK_PRODUCT_HIGHLIGHT)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(ITEM_LIST, itemListValue)
            putParcelableArrayList(
                ITEMS,
                arrayListOf(
                    createProductHighlightItemMap(
                        trackerDataModel,
                        itemListValue
                    )
                )
            )
            putString(PRODUCT_ID, trackerDataModel.productId)
            putString(SHOP_ID, trackerDataModel.shopId)
            putString(USER_ID, trackerDataModel.userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventBundle)
    }

    private fun createProductHighlightItemMap(
        trackerDataModel: ShopCampaignWidgetProductHighlightTrackerModel,
        itemListValue: String
    ): Bundle {
        return Bundle().apply {
            putString(DIMENSION_40, itemListValue)
            putInt(INDEX, trackerDataModel.position)
            putString(ITEM_BRAND, "")
            putString(ITEM_CATEGORY, "")
            putString(ITEM_ID, trackerDataModel.productId)
            putString(ITEM_NAME, trackerDataModel.productName)
            putString(ITEM_VARIANT, "")
            putString(PRICE, ShopUtil.formatPrice(trackerDataModel.productPrice))
        }
    }

    fun sendImpressionShopVoucherSliderCampaignTracker(
        trackerDataModel: ShopCampaignWidgetVoucherSliderTrackerModel
    ) {
        val eventLabelValue = ShopUtil.joinDash(
            "",
            trackerDataModel.widgetId
        )
        val eventMap = mapOf(
            EVENT to VIEW_PG_IRIS,
            EVENT_ACTION to IMPRESSION_VOUCHER_SLIDER_WIDGET,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to eventLabelValue,
            TRACKER_ID to TRACKER_ID_IMPRESSION_VOUCHER_SLIDER,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to trackerDataModel.shopId,
            USER_ID to trackerDataModel.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun clickCtaVoucherSliderItem(trackerDataModel: ShopCampaignWidgetVoucherSliderItemClickCtaTrackerModel) {
        val eventLabelValue = ShopUtil.joinDash(
            trackerDataModel.buttonStr,
            trackerDataModel.widgetId
        )
        val eventMap = mapOf(
            EVENT to CLICK_PG,
            EVENT_ACTION to CLICK_CTA_VOUCHER_SLIDER_ITEM_WIDGET,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to eventLabelValue,
            TRACKER_ID to TRACKER_ID_CLICK_CTA_VOUCHER_SLIDER_ITEM,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to trackerDataModel.shopId,
            USER_ID to trackerDataModel.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun clickSeeMoreVoucherSlider(trackerDataModel: ClickSeeMoreVoucherSliderItemTrackerModel) {
        val eventLabelValue = ShopUtil.joinDash(
            "",
            trackerDataModel.widgetId
        )
        val eventMap = mapOf(
            EVENT to CLICK_PG,
            EVENT_ACTION to CLICK_SEE_MORE_VOUCHER_SLIDER_ITEM_WIDGET,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to eventLabelValue,
            TRACKER_ID to TRACKER_ID_CLICK_SEE_MORE_VOUCHER_SLIDER_ITEM,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to trackerDataModel.shopId,
            USER_ID to trackerDataModel.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun clickVoucherSliderItem(trackerDataModel: ShopCampaignWidgetVoucherSliderItemClickTrackerModel) {
        val eventLabelValue = ShopUtil.joinDash(
            "",
            trackerDataModel.widgetId
        )
        val eventMap = mapOf(
            EVENT to CLICK_PG,
            EVENT_ACTION to CLICK_VOUCHER_SLIDER_ITEM_WIDGET,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to eventLabelValue,
            TRACKER_ID to TRACKER_ID_CLICK_VOUCHER_SLIDER_ITEM,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to trackerDataModel.shopId,
            USER_ID to trackerDataModel.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun impressionWidgetDisplay(trackerDataModel: ShopCampaignWidgetDisplayTrackerDataModel) {
        val eventLabelValue = ShopUtil.joinDash(
            "",
            trackerDataModel.widgetId
        )
        val eventBundle = Bundle().apply {
            putString(EVENT, VIEW_ITEM)
            putString(EVENT_ACTION, IMPRESSION_BANNER_WIDGET)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, eventLabelValue)
            putString(TRACKER_ID, TRACKER_ID_IMPRESSION_BANNER_WIDGET)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putParcelableArrayList(
                ITEMS,
                arrayListOf(
                    createBannerWidgetItemMap(
                        trackerDataModel,
                    )
                )
            )
            putString(SHOP_ID, trackerDataModel.shopId)
            putString(USER_ID, trackerDataModel.userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM, eventBundle)
    }

    private fun createBannerWidgetItemMap(
        trackerDataModel: ShopCampaignWidgetDisplayTrackerDataModel
    ): Bundle {
        return Bundle().apply {
            putString(CREATIVE_NAME, trackerDataModel.name)
            putInt(CREATIVE_SLOT, trackerDataModel.position)
            putString(ITEM_ID, "")
            putString(ITEM_NAME, trackerDataModel.widgetTitle)
        }
    }

    fun clickWidgetDisplay(trackerDataModel: ShopCampaignWidgetDisplayTrackerDataModel) {
        val eventLabelValue = ShopUtil.joinDash(
            "",
            trackerDataModel.widgetId
        )
        val eventBundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_ACTION, CLICK_BANNER_WIDGET)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, eventLabelValue)
            putString(TRACKER_ID, TRACKER_ID_CLICK_BANNER_WIDGET)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putParcelableArrayList(
                ITEMS,
                arrayListOf(
                    createBannerWidgetItemMap(
                        trackerDataModel,
                    )
                )
            )
            putString(SHOP_ID, trackerDataModel.shopId)
            putString(USER_ID, trackerDataModel.userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventBundle)
    }

    fun sendImpressionProductSliderBannerHighlight(
        trackerDataModel: ShopCampaignProductSliderBannerHighlightTrackerDataModel
    ) {
        val eventLabelValue = ShopUtil.joinDash(
            "",
            trackerDataModel.widgetId
        )
        val itemListValue = ShopUtil.joinDash(SHOPPAGE, HOME_ANCHOR, trackerDataModel.selectedTabName)
        val eventBundle = Bundle().apply {
            putString(EVENT, VIEW_ITEM_LIST)
            putString(EVENT_ACTION, IMPRESSION_PRODUCT_SLIDER_BANNER_HIGHLIGHT)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, eventLabelValue)
            putString(TRACKER_ID, TRACKER_ID_IMPRESSION_SLIDER_BANNER_HIGHLIGHT_WIDGET)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(ITEM_LIST, itemListValue)
            putParcelableArrayList(
                ITEMS,
                arrayListOf(
                    createProductSliderBannerHighlightItemMap(
                        trackerDataModel,
                        itemListValue
                    )
                )
            )
            putString(PRODUCT_ID, trackerDataModel.getProductIdFromAppLink())
            putString(SHOP_ID, trackerDataModel.shopId)
            putString(USER_ID, trackerDataModel.userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM_LIST, eventBundle)
    }

    private fun createProductSliderBannerHighlightItemMap(
        trackerDataModel: ShopCampaignProductSliderBannerHighlightTrackerDataModel,
        itemListValue: String
    ): Bundle {
        return Bundle().apply {
            putString(DIMENSION_40, itemListValue)
            putInt(INDEX, trackerDataModel.position)
            putString(ITEM_BRAND, "")
            putString(ITEM_CATEGORY, "")
            putString(ITEM_ID, trackerDataModel.getProductIdFromAppLink())
            putString(ITEM_NAME, "")
            putString(ITEM_VARIANT, trackerDataModel.selectedTabName)
            putDouble(PRICE, 0.toDouble())
        }
    }

    fun sendClickProductSliderBannerHighlight(trackerDataModel: ShopCampaignProductSliderBannerHighlightTrackerDataModel) {
        val eventLabelValue = ShopUtil.joinDash(
            "",
            trackerDataModel.widgetId
        )
        val itemListValue = ShopUtil.joinDash(SHOPPAGE, HOME_ANCHOR, trackerDataModel.selectedTabName)
        val eventBundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_ACTION, CLICK_PRODUCT_SLIDER_BANNER_HIGHLIGHT)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, eventLabelValue)
            putString(TRACKER_ID, TRACKER_ID_CLICK_PRODUCT_SLIDER_BANNER_HIGHLIGHT_WIDGET)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(ITEM_LIST, itemListValue)
            putParcelableArrayList(
                ITEMS,
                arrayListOf(
                    createProductSliderBannerHighlightItemMap(
                        trackerDataModel,
                        itemListValue
                    )
                )
            )
            putString(PRODUCT_ID, trackerDataModel.getProductIdFromAppLink())
            putString(SHOP_ID, trackerDataModel.shopId)
            putString(USER_ID, trackerDataModel.userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventBundle)
    }

    fun sendClickCtaSliderBannerHighlight(trackerDataModel: ShopCampaignClickCtaSliderBannerHighlightTrackerDataModel) {
        val eventLabelValue = ShopUtil.joinDash(
            "",
            trackerDataModel.widgetId
        )
        val eventMap = mapOf(
            EVENT to CLICK_PG,
            EVENT_ACTION to CLICK_CTA_SLIDER_BANNER_HIGHLIGHT,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to eventLabelValue,
            TRACKER_ID to TRACKER_ID_CLICK_EXPLORE_SHOP_SLIDER_BANNER_HIGHLIGHT_WIDGET,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to trackerDataModel.shopId,
            USER_ID to trackerDataModel.userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun impressionPlayWidgetItem(trackerDataModel: ShopCampaignPlayWidgetItemTrackerDataModel) {
        val eventLabelValue = ShopUtil.joinDash(
            trackerDataModel.campaignId,
            trackerDataModel.widgetId,
            trackerDataModel.channelId
        )
        val itemListValue = ShopUtil.joinDash(SHOPPAGE, PLAY_WIDGET, trackerDataModel.tabName)
        val eventBundle = Bundle().apply {
            putString(EVENT, VIEW_ITEM_LIST)
            putString(EVENT_ACTION, IMPRESSION_CAMPAIGN_PLAY_WIDGET_ITEM)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, eventLabelValue)
            putString(TRACKER_ID, TRACKER_ID_IMPRESSION_PLAY_WIDGET)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(ITEM_LIST, itemListValue)
            putParcelableArrayList(
                ITEMS,
                arrayListOf(
                    createPlayWidgetItemMap(
                        trackerDataModel,
                        itemListValue
                    )
                )
            )
            putString(SHOP_ID, trackerDataModel.shopId)
            putString(USER_ID, trackerDataModel.userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM_LIST, eventBundle)
    }

    fun clickPlayWidgetItem(trackerDataModel: ShopCampaignPlayWidgetItemTrackerDataModel) {
        val eventLabelValue = ShopUtil.joinDash(
            trackerDataModel.campaignId,
            trackerDataModel.widgetId,
            trackerDataModel.channelId
        )
        val itemListValue = ShopUtil.joinDash(SHOPPAGE, PLAY_WIDGET, trackerDataModel.tabName)
        val eventBundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_ACTION, CLICK_CAMPAIGN_PLAY_WIDGET_ITEM)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, eventLabelValue)
            putString(TRACKER_ID, TRACKER_ID_CLICK_PLAY_WIDGET)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(ITEM_LIST, itemListValue)
            putParcelableArrayList(
                ITEMS,
                arrayListOf(
                    createPlayWidgetItemMap(
                        trackerDataModel,
                        itemListValue
                    )
                )
            )
            putString(SHOP_ID, trackerDataModel.shopId)
            putString(USER_ID, trackerDataModel.userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventBundle)
    }

    private fun createPlayWidgetItemMap(
        trackerDataModel: ShopCampaignPlayWidgetItemTrackerDataModel,
        itemListValue: String
    ): Bundle {
        return Bundle().apply {
            putString(DIMENSION_40, itemListValue)
            putInt(INDEX, trackerDataModel.position)
            putString(ITEM_BRAND, "")
            putString(ITEM_CATEGORY, "")
            putString(ITEM_ID, "")
            putString(ITEM_NAME, "")
            putString(ITEM_VARIANT, trackerDataModel.tabName)
            putDouble(PRICE, Int.ZERO.toDouble())
        }
    }
}
