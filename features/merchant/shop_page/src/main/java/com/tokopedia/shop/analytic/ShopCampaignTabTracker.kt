package com.tokopedia.shop.analytic

import android.os.Bundle
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.BUSINESS_UNIT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CAMPAIGN_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CAMPAIGN_TAB_PRODUCT_CLICK_EVENT_ACTION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CAMPAIGN_TAB_PRODUCT_IMPRESSION_EVENT_ACTION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOP_BANNER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOP_BANNER_LABEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CREATIVE_NAME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CREATIVE_SLOT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CURRENT_SITE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_117
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_118
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_40
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_ACTION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_CATEGORY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_LABEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_SHOP_BANNER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_SHOP_BANNER_LABEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.INDEX
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEMS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_BRAND
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_CATEGORY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_LIST
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_NAME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_VARIANT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.LABEL_CLICK_PRODUCT_LIST
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.LABEL_IMPRESSION_PRODUCT_LIST
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PHYSICAL_GOODS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRICE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PROMOTIONS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SELECT_CONTENT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOPPAGE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_CAMPAIGN_TAB_EVENT_CATEGORY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_BUYER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TRACKER_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.USER_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VIEW_ITEM
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VIEW_ITEM_LIST
import com.tokopedia.track.TrackApp
import javax.inject.Inject

/*
Data Layer Docs:

Campaign tab shop page widget
https://mynakama.tokopedia.com/datatracker/product/requestdetail/view/3199

 */

class ShopCampaignTabTracker @Inject constructor() {

    fun impressionShopBannerWidget(
        shopId: String,
        widgetName: String,
        widgetId: String,
        position: Int,
        userId: String
    ) {
        val eventLabel = String.format(IMPRESSION_SHOP_BANNER_LABEL, widgetName, position)
        val eventBundle = Bundle().apply {
            putString(EVENT, VIEW_ITEM)
            putString(EVENT_ACTION, IMPRESSION_SHOP_BANNER)
            putString(EVENT_CATEGORY, SHOP_CAMPAIGN_TAB_EVENT_CATEGORY)
            putString(EVENT_LABEL, eventLabel)
            putString(TRACKER_ID, TrackerId.IMPRESSION_SHOP_BANNER)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(SHOP_ID, shopId)
            putString(USER_ID, userId)
            putParcelableArrayList(
                PROMOTIONS,
                arrayListOf(
                    createShopBannerWidgetPromotions(
                        widgetName,
                        widgetId,
                        position
                    )
                )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM, eventBundle)
    }

    private fun createShopBannerWidgetPromotions(
        widgetName: String,
        widgetId: String,
        position: Int
    ): Bundle {
        return Bundle().apply {
            putString(CREATIVE_NAME, widgetName)
            putInt(CREATIVE_SLOT, position)
            putString(ITEM_ID, widgetId)
            putString(ITEM_NAME, widgetName)
        }
    }

    fun clickShopBannerWidget(
        shopId: String,
        widgetName: String,
        widgetId: String,
        position: Int,
        userId: String
    ) {
        val eventLabel = String.format(CLICK_SHOP_BANNER_LABEL, widgetName, position)
        val eventBundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_ACTION, CLICK_SHOP_BANNER)
            putString(EVENT_CATEGORY, SHOP_CAMPAIGN_TAB_EVENT_CATEGORY)
            putString(EVENT_LABEL, eventLabel)
            putString(TRACKER_ID, TrackerId.CLICK_SHOP_BANNER)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(SHOP_ID, shopId)
            putString(USER_ID, userId)
            putParcelableArrayList(
                PROMOTIONS,
                arrayListOf(
                    createShopBannerWidgetPromotions(
                        widgetName,
                        widgetId,
                        position
                    )
                )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventBundle)
    }

    fun impressionSeeEntryPointMerchantVoucherCouponTokoMemberInformation(
        shopId: String
    ) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to ShopPageTrackingConstant.VIEW_SHOP_PAGE_IRIS,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_ACTION to ShopPageTrackingConstant.VIEW_COUPON_TOKO_MEMBER,
            EVENT_LABEL to ShopPageTrackingConstant.SHOP_PAGE_LABEL + shopId,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun clickCampaignTabProduct(
        productId: String,
        productName: String,
        productPrice: Long,
        widgetName: String,
        shopId: String,
        userId: String,
        widgetTitle: String,
        itemPosition: Int,
        bundlingType: String = "",
        bundlingId: String = ""
    ) {
        val eventBundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_ACTION, CAMPAIGN_TAB_PRODUCT_CLICK_EVENT_ACTION)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, String.format(LABEL_CLICK_PRODUCT_LIST, CAMPAIGN_TAB, widgetTitle))
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(PRODUCT_ID, productId)
            putString(SHOP_ID, shopId)
            putString(USER_ID, userId)
            putString(ITEM_LIST, "$SHOPPAGE - $widgetName")
            putParcelableArrayList(
                ITEMS,
                arrayListOf(
                    createProductCampaignItems(
                        itemPosition,
                        productId,
                        productName,
                        productPrice,
                        widgetName,
                        bundlingType,
                        bundlingId
                    )
                )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventBundle)
    }

    fun impressionCampaignTabProduct(
        productId: String,
        productName: String,
        productPrice: Long,
        widgetName: String,
        shopId: String,
        userId: String,
        widgetTitle: String,
        itemPosition: Int,
        bundlingType: String = "",
        bundlingId: String = ""
    ) {
        val eventLabel = String.format(LABEL_IMPRESSION_PRODUCT_LIST, CAMPAIGN_TAB, widgetTitle)
        val eventBundle = Bundle().apply {
            putString(EVENT, VIEW_ITEM_LIST)
            putString(EVENT_ACTION, CAMPAIGN_TAB_PRODUCT_IMPRESSION_EVENT_ACTION)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, eventLabel)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(PRODUCT_ID, productId)
            putString(SHOP_ID, shopId)
            putString(USER_ID, userId)
            putParcelableArrayList(
                ITEMS,
                arrayListOf(
                    createProductCampaignItems(
                        itemPosition,
                        productId,
                        productName,
                        productPrice,
                        widgetName,
                        bundlingType,
                        bundlingId
                    )
                )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM_LIST, eventBundle)
    }

    private fun createProductCampaignItems(
        position: Int,
        productId: String,
        productName: String,
        productPrice: Long,
        widgetName: String,
        bundlingType: String,
        bundlingId: String
    ): Bundle {
        return Bundle().apply {
            if (bundlingType.isNotEmpty())
                putString(DIMENSION_117, bundlingType)
            if (bundlingId.isNotEmpty())
                putString(DIMENSION_118, bundlingId)
            putString(DIMENSION_40, "$SHOPPAGE - $widgetName")
            putInt(INDEX, position)
            putString(ITEM_BRAND, "")
            putString(ITEM_CATEGORY, "")
            putString(ITEM_ID, productId)
            putString(ITEM_NAME, productName)
            putString(ITEM_VARIANT, CAMPAIGN_TAB)
            putLong(PRICE, productPrice)
        }
    }
}
