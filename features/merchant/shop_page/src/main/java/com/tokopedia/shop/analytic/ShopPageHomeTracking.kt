package com.tokopedia.shop.analytic

import android.os.Bundle
import com.tokopedia.atc_common.domain.model.response.AddToCartBundleModel
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.digitsOnly
import com.tokopedia.kotlin.extensions.view.getDigits
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.merchantvoucher.common.model.MerchantVoucherViewModel
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ACTION_CLICK_PRODUCT_LIST_TOGGLE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ACTION_FIELD
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ACTION_HOME_TAB_IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ACTION_SHOP_DECOR_CLICK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ACTION_SHOP_DECOR_IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ADD
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ADD_TO_CART
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ALL_PRODUCT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.BOE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.BO_PRODUCT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.BRAND
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.BUNDLE_ADD_TO_CART
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.BUNDLING_ADD_TO_CART
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.BUSINESS_UNIT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.BUYER_RECOMMENDATION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CAMPAIGN_SEGMENTATION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CATEGORY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CATEGORY_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_ACTIVATE_REMINDER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_ADD_TO_CART
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_ATC_RECOMMENDATION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_BACK_BUTTON
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_CLOSE_FILTER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_CLOSE_TNC
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_DEACTIVATE_REMINDER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_DIGITAL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_DONATION_BY_SELLER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_ETALASE_NAVIGATION_BANNER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_FILTER_CHIP
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_FOLLOW_UNFOLLOW_TNC_PAGE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_OCC_RECOMMENDATION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_OK_TOASTER_NOTIFY_ME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_PG
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_PRODUCT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_PRODUCT_RECOMMENDATION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_PRODUCT_SHOP_DECOR
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOP_PAGE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_SHOWCASE_LIST
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_TNC
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_TNC_BUTTON_FLASH_SALE_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_CTA_SEE_ALL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CLICK_WISHLIST
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CREATIVE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CREATIVE_NAME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CREATIVE_SLOT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CREATIVE_URL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CURRENCY_CODE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.CURRENT_SITE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_117
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_118
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_38
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_40
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_45
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_61
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_79
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_80
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_81
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_82
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_83
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_87
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DIMENSION_90
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.DISPLAY_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ECOMMERCE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ETALASE_NAVIGATION_BANNER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ETALASE_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ETALASE_X
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_ACTION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_CATEGORY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EVENT_LABEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.Event.DIRECT_PURCHASE_ADD_TO_CART
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.Event.VIEW_PG_IRIS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.ALL_PRODUCT_CLICKED
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.ALL_PRODUCT_IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CAMPAIGN_WIDGET_CLICK_BANNER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CAMPAIGN_WIDGET_CLICK_CTA_SEE_ALL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CAMPAIGN_WIDGET_CLICK_REMINDER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CAMPAIGN_WIDGET_IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CAMPAIGN_WIDGET_PRODUCT_CARD_CLICK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CAMPAIGN_WIDGET_PRODUCT_CARD_IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_EXCLUSIVE_LAUNCH_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_PERSONALIZATION_TRENDING_WIDGET_ITEM
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_PERSO_PRODUCT_COMPARISON
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_PRODUCT_ATC
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_PRODUCT_ATC_QUANTITY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_PRODUCT_ATC_RESET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_REIMAGINED_BANNER_PRODUCT_CAROUSEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_REIMAGINED_BANNER_SHOWCASE_NAVIGATION_WITH_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_REIMAGINED_PRODUCT_CAROUSEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_REIMAGINED_SHOWCASE_NAVIGATION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_REIMAGINED_SHOWCASE_NAVIGATION_VIEW_ALL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_REIMAGINED_SHOWCASE_NAVIGATION_WITH_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.CLICK_REMIND_ME_EXCLUSIVE_LAUNCH_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.IMPRESSION_EXCLUSIVE_LAUNCH_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.IMPRESSION_PERSONALIZATION_TRENDING_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.IMPRESSION_PERSONALIZATION_TRENDING_WIDGET_ITEM
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.IMPRESSION_PERSO_PRODUCT_COMPARISON
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.IMPRESSION_PRODUCT_ATC
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.IMPRESSION_REIMAGINED_BANNER_PRODUCT_CAROUSEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.IMPRESSION_REIMAGINED_SHOWCASE_NAVIGATION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventAction.IMPRESSION_REIMAGINED_SHOWCASE_NAVIGATION_WITH_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.EventCategory.SHOP_PAGE_BUYER_DIRECT_PURCHASE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.FESTIVITY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.FLASH_SALE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.FOLLOW
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.GENERAL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.HOME_AND_BROWSE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.HOME_DISPLAY_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.HOME_FEATURED_PRODUCT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.HOME_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.IDR
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSIONS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_DONATION_BY_SELLER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_ETALASE_NAVIGATION_BANNER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_PRODUCT_RECOMMENDATION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_TNC
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.IMPRESSION_TOASTER_NOTIFY_ME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.INDEX
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEMS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEMS_SHOP_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEMS_SHOP_TYPE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_BRAND
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_CATEGORY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_LIST
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_LIST_PERSO_PRODUCT_COMPARISON
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_LIST_PERSO_TRENDING_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_NAME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.ITEM_VARIANT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.LABEL_SHOP_DECOR_CLICK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.LABEL_SHOP_DECOR_IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.LIST
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.LOGIN
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.MERCHANT_VOUCHER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.MULTIPLE_BUNDLE_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.MULTIPLE_TYPE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.NAME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.NONE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.NON_BO_PRODUCT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.NON_LOGIN
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.NOT_SEARCH_RESULT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PACKAGE_VARIANT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PAGE_SOURCE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PAGE_TYPE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PHYSICAL_GOODS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PHYSICAL_GOODS_PASCAL_CASE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.POSITION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRICE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCTS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_BUNDLING
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_CLICK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_LIST_IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_LIST_IMPRESSION_SHOP_DECOR
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PRODUCT_VIEW
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PROMOTIONS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PROMO_CLICK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PROMO_CODE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PROMO_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.PROMO_VIEW
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.QUANTITY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.REMOVE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SCREEN_SHOP_PAGE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SEE_ENTRY_POINT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SELECTED_ETALASE_CHIP
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SELECT_CONTENT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_NAME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_BUYER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_DONATION_BY_SELLER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PAGE_LABEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PRODUCT_ATC_QUANTITY_DECREASE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_PRODUCT_ATC_QUANTITY_INCREASE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SHOP_TYPE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SINGLE_BUNDLE_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.SINGLE_TYPE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.THEMATIC_WIDGET_IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.THEMATIC_WIDGET_PRODUCT_CARD_CLICK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.THEMATIC_WIDGET_PRODUCT_CARD_IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.THEMATIC_WIDGET_PRODUCT_CARD_SEE_ALL_CLICK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.THEMATIC_WIDGET_SEE_ALL_CLICK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TOKOPEDIA_MARKETPLACE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TRACKER_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_CLICK_SINGLE_BUNDLING_WIDGET_PACKAGE_VARIANT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_ALL_PRODUCT_CLICKED
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_ALL_PRODUCT_IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_ATC_CLICK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_ATC_CLICK_DELETE
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_ATC_CLICK_QUANTITY
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_ATC_IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_ATC_MULTIPLE_BUNDLING_WDIGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_ATC_SINGLE_BUNDLING_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CAMPAIGN_WIDGET_CLICK_BANNER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CAMPAIGN_WIDGET_CLICK_CTA_SEE_ALL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CAMPAIGN_WIDGET_CLICK_REMINDER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CAMPAIGN_WIDGET_IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CAMPAIGN_WIDGET_PRODUCT_CARD_CLICK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CAMPAIGN_WIDGET_PRODUCT_CARD_IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_CTA_BANNER_TIMER_HOME_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_MULTIPLE_BUNDLE_PRODUCT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_PERSONALIZATION_TRENDING_WIDGET_ITEM
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_PRODUCT_SHOP_DECOR
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_REIMAGINED_BANNER_PRODUCT_CAROUSEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_REIMAGINED_BANNER_SHOWCASE_NAVIGATION_WITH_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_REIMAGINED_PRODUCT_CAROUSEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_REIMAGINED_SHOWCASE_NAVIGATION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_REIMAGINED_SHOWCASE_NAVIGATION_WITH_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_REMIND_ME_BANNER_TIMER_HOME_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_SHOP_DECOR
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_SHOP_PERSO_PRODUCT_COMPARISON
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_SINGLE_BUNDLE_PRODUCT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_CLICK_VIEW_ALL_REIMAGINED_SHOWCASE_NAVIGATION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_BANNER_TIMER_HOME_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_MULTIPLE_BUNDLING_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_PERSONALIZATION_TRENDING_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_PERSONALIZATION_TRENDING_WIDGET_ITEM
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_REIMAGINED_BANNER_PRODUCT_CAROUSEL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_REIMAGINED_SHOWCASE_NAVIGATION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_REIMAGINED_SHOWCASE_NAVIGATION_WITH_TAB
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_SHOP_DECOR
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_SHOP_PERSO_PRODUCT_COMPARISON
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_IMPRESSION_SINGLE_BUNDLING_WIDGET
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_PRODUCT_CAROUSEL_CLICK_CTA_SEE_ALL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_PRODUCT_LIST_IMPRESSION_SHOP_DECOR
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_THEMATIC_WIDGET_IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_THEMATIC_WIDGET_PRODUCT_CARD_CLICK
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.TrackerId.TRACKER_ID_THEMATIC_WIDGET_PRODUCT_CARD_IMPRESSION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.UNFOLLOW
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.USER_ID
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_FINISHED_BANNER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_FINISHED_CAMPAIGN
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_HOME
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_MULTIPLE_BUNDLING
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_NO_SEE_CAMPAIGN
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_ONGOING
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_ONGOING_BANNER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_ONGOING_CAMPAIGN
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_SEE_CAMPAIGN
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_SHOP_DECOR
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_SHOP_PAGE_THEMATIC
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_SINGLE_BUNDLING
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_STRING_ZERO
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_UPCOMING
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_UPCOMING_BANNER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VALUE_UPCOMING_CAMPAIGN
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VARIANT
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VERTICAL_POSITION
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VIEW_COUPON_TOKO_MEMBER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VIEW_DIGITAL_IRIS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VIEW_ITEM
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VIEW_ITEM_LIST
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.VIEW_SHOP_PAGE_IRIS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.WIDGET_TYPE_ADD_ONS
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.WIDGET_TYPE_BUY_AGAIN
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.WIDGET_TYPE_CAROUSELL
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.WIDGET_TYPE_REMINDER
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.WISHLIST
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.WITHOUT_CART
import com.tokopedia.shop.analytic.ShopPageTrackingConstant.WITH_CART
import com.tokopedia.shop.analytic.model.*
import com.tokopedia.shop.common.constant.PMAX_PARAM_KEY
import com.tokopedia.shop.common.constant.PMIN_PARAM_KEY
import com.tokopedia.shop.common.constant.RATING_PARAM_KEY
import com.tokopedia.shop.common.data.model.ShopPageAtcTracker
import com.tokopedia.shop.common.util.ShopProductViewGridType
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.common.widget.bundle.model.ShopHomeBundleProductUiModel
import com.tokopedia.shop.home.WidgetName.ADD_ONS
import com.tokopedia.shop.home.WidgetName.BUY_AGAIN
import com.tokopedia.shop.home.WidgetName.RECENT_ACTIVITY
import com.tokopedia.shop.home.view.model.NotifyMeAction
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeShowcaseListItemUiModel
import com.tokopedia.shop.home.view.model.ShopWidgetDisplayBannerTimerUiModel
import com.tokopedia.shop.home.view.model.StatusCampaign
import com.tokopedia.shop.home.view.model.banner_product_group.ShopWidgetComponentBannerProductGroupUiModel
import com.tokopedia.shop.home.view.model.banner_product_group.appearance.ProductItemType
import com.tokopedia.shop.home.view.model.showcase_navigation.Showcase
import com.tokopedia.shop.home.view.model.showcase_navigation.ShowcaseNavigationUiModel
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.CarouselAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.LeftMainBannerAppearance
import com.tokopedia.shop.home.view.model.showcase_navigation.appearance.TopMainBannerAppearance
import com.tokopedia.shop_widget.thematicwidget.uimodel.ProductCardUiModel
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.builder.Tracker
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.shop.home.view.model.banner_product_group.ShopWidgetComponentBannerProductGroupUiModel.WidgetStyle
/*
Data Layer Docs:

Shop Page Home Revamp
https://docs.google.com/spreadsheets/d/1l91ritx5rj-RJzcTNVXnMTcOp3sWZz6O2v__nfV64Co/edit#gid=306885993

Mvc + Shop Follower Revamp
https://mynakama.tokopedia.com/datatracker/requestdetail/690
 */

class ShopPageHomeTracking(
    trackingQueue: TrackingQueue
) : ShopPageTracking(trackingQueue) {

    fun impressionDisplayWidget(
        isOwner: Boolean,
        shopId: String,
        layoutId: String,
        widgetName: String,
        widgetId: String,
        positionVertical: Int,
        widgetOption: String,
        destinationLink: String,
        assetUrl: String,
        positionHorizontal: Int,
        customDimensionShopPage: CustomDimensionShopPage
    ) {
        val eventLabel = joinDash(shopId, DISPLAY_WIDGET, layoutId, widgetName)
        val eventMap = createMap(
            PROMO_VIEW,
            getShopPageCategory(isOwner),
            IMPRESSION,
            eventLabel,
            customDimensionShopPage
        )
        eventMap[ECOMMERCE] = mutableMapOf(
            PROMO_VIEW to mutableMapOf(
                PROMOTIONS to mutableListOf(
                    createDisplayWidgetPromotionsItemMap(
                        widgetId,
                        positionVertical,
                        widgetName,
                        widgetOption,
                        destinationLink,
                        assetUrl,
                        positionHorizontal,
                        customDimensionShopPage.shopType.orEmpty(),
                        shopId
                    )
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickDisplayWidget(
        isOwner: Boolean,
        shopId: String,
        layoutId: String,
        widgetName: String,
        widgetId: String,
        positionVertical: Int,
        widgetOption: String,
        destinationLink: String,
        assetUrl: String,
        positionHorizontal: Int,
        customDimensionShopPage: CustomDimensionShopPage
    ) {
        val eventLabel = joinDash(shopId, DISPLAY_WIDGET, layoutId, widgetName)
        val eventMap = createMap(
            PROMO_CLICK,
            getShopPageCategory(isOwner),
            CLICK,
            eventLabel,
            customDimensionShopPage
        )
        eventMap[ECOMMERCE] = mutableMapOf(
            PROMO_CLICK to mutableMapOf(
                PROMOTIONS to mutableListOf(
                    createDisplayWidgetPromotionsItemMap(
                        widgetId,
                        positionVertical,
                        widgetName,
                        widgetOption,
                        destinationLink,
                        assetUrl,
                        positionHorizontal,
                        customDimensionShopPage.shopType.orEmpty(),
                        shopId
                    )
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    fun impressionSeeEntryPointMerchantVoucherCoupon(
        shopId: String,
        userId: String?
    ) {
        followUnfollowShop(
            VIEW_SHOP_PAGE_IRIS,
            SEE_ENTRY_POINT,
            SHOP_PAGE_LABEL + shopId,
            userId
        )
    }

    fun impressionSeeEntryPointMerchantVoucherCouponTokoMemberInformation(
        shopId: String
    ) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to VIEW_SHOP_PAGE_IRIS,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_ACTION to VIEW_COUPON_TOKO_MEMBER,
            EVENT_LABEL to SHOP_PAGE_LABEL + shopId,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE
        )
        sendDataLayerEvent(eventMap)
    }

    fun impressionProductPersonalization(
        isOwner: Boolean,
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        recommendationType: String,
        categoryBreadcrumbs: String,
        userId: String,
        shopName: String,
        horizontalPosition: Int,
        widgetHeaderTitle: String,
        widgetName: String,
        customDimensionShopPage: CustomDimensionShopPage
    ) {
        val widgetType = getShopPersoWidgetType(widgetName)
        val eventAction = joinDash(IMPRESSION_PRODUCT_RECOMMENDATION, LOGIN)
        val eventLabel = joinDash(widgetHeaderTitle, widgetType)
        val actionFieldList = joinDash(
            SHOPPAGE,
            LOGIN,
            BUYER_RECOMMENDATION,
            recommendationType,
            widgetType
        )
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to PRODUCT_VIEW,
            EVENT_CATEGORY to getShopPageCategory(isOwner),
            EVENT_ACTION to eventAction,
            EVENT_LABEL to eventLabel,
            BUSINESS_UNIT to HOME_AND_BROWSE,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
            SHOP_TYPE to customDimensionShopPage.shopType.orEmpty(),
            USER_ID to userId
        )
        eventMap[ECOMMERCE] = mutableMapOf(
            CURRENCY_CODE to IDR,
            IMPRESSIONS to mutableListOf(
                createProductPersonalizationItemMap(
                    productName,
                    productId,
                    productDisplayedPrice,
                    shopName,
                    categoryBreadcrumbs,
                    horizontalPosition,
                    actionFieldList
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    fun impressionProductPersonalizationReminder(
        isOwner: Boolean,
        isLogin: Boolean,
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        recommendationType: String,
        userId: String,
        shopName: String,
        horizontalPosition: Int,
        widgetHeaderTitle: String,
        customDimensionShopPage: CustomDimensionShopPage,
        categoryBreadcrumbs: String
    ) {
        val eventAction = IMPRESSION_PRODUCT_RECOMMENDATION
        var eventLabel = joinDash(widgetHeaderTitle, WIDGET_TYPE_REMINDER)
        if (!isLogin) {
            eventLabel = joinDash(eventLabel, NON_LOGIN)
        }
        val loginNonLoginEventValue = if (isLogin) LOGIN else NON_LOGIN
        val actionFieldList = joinDash(
            SHOPPAGE,
            loginNonLoginEventValue,
            BUYER_RECOMMENDATION,
            recommendationType,
            WIDGET_TYPE_REMINDER
        )
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to PRODUCT_VIEW,
            EVENT_CATEGORY to getShopPageCategory(isOwner),
            EVENT_ACTION to eventAction,
            EVENT_LABEL to eventLabel,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            PAGE_TYPE to SHOPPAGE,
            SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
            SHOP_TYPE to customDimensionShopPage.shopType.orEmpty(),
            USER_ID to userId
        )
        eventMap[ECOMMERCE] = mutableMapOf(
            CURRENCY_CODE to IDR,
            IMPRESSIONS to mutableListOf(
                createProductPersonalizationItemMap(
                    productName,
                    productId,
                    productDisplayedPrice,
                    shopName,
                    categoryBreadcrumbs,
                    horizontalPosition,
                    actionFieldList
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    private fun getShopPersoWidgetType(widgetName: String): String {
        return when (widgetName) {
            BUY_AGAIN -> WIDGET_TYPE_BUY_AGAIN
            RECENT_ACTIVITY -> WIDGET_TYPE_CAROUSELL
            ADD_ONS -> WIDGET_TYPE_ADD_ONS
            else -> ""
        }
    }

    fun impressionProduct(
        isLogin: Boolean,
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        shopName: String,
        horizontalPosition: Int,
        customDimensionShopPage: CustomDimensionShopPageAttribution,
        sortAndFilterValue: String,
        userId: String,
        selectedTabName: String
    ) {
        val etalaseChip = String.format(SELECTED_ETALASE_CHIP, ALL_PRODUCT)
        val loginNonLoginEventValue = if (isLogin) LOGIN else NON_LOGIN
        val listEventValue = joinDash(SHOPPAGE, customDimensionShopPage.shopId, etalaseChip, loginNonLoginEventValue, NOT_SEARCH_RESULT)
        val eventAction = joinDash(ALL_PRODUCT_IMPRESSION, etalaseChip, loginNonLoginEventValue, NOT_SEARCH_RESULT)
        val eventMap = createMap(
            PRODUCT_VIEW,
            SHOP_PAGE_BUYER,
            eventAction,
            "",
            customDimensionShopPage
        )
        eventMap[TRACKER_ID] = TRACKER_ID_ALL_PRODUCT_IMPRESSION
        eventMap[BUSINESS_UNIT] = PHYSICAL_GOODS
        eventMap[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        eventMap[ITEM_LIST] = listEventValue
        eventMap[ECOMMERCE] = mutableMapOf(
            CURRENCY_CODE to IDR,
            IMPRESSIONS to mutableListOf(
                createProductItemMap(
                    productName,
                    productId,
                    productDisplayedPrice,
                    shopName,
                    horizontalPosition,
                    customDimensionShopPage,
                    sortAndFilterValue,
                    listEventValue,
                    selectedTabName
                )
            )
        )
        eventMap[SHOP_ID] = customDimensionShopPage.shopId.orEmpty()
        eventMap[USER_ID] = userId
        sendDataLayerEvent(eventMap)
    }

    fun impressionProductShopDecoration(trackerModel: ProductShopDecorationTrackerDataModel) {
        with(trackerModel) {
            val cartTrackerValue = if (widgetOption == Int.ONE) WITH_CART else WITHOUT_CART
            var eventLabel = joinDash(
                HOME_TAB,
                verticalPosition.toString(),
                shopId,
                widgetName,
                widgetMasterId,
                cartTrackerValue
            )
            if (isFestivity) {
                eventLabel = joinDash(eventLabel, FESTIVITY)
            }
            val itemList = joinDash(SHOPPAGE, eventLabel)
            val eventBundle = Bundle().apply {
                putString(EVENT, VIEW_ITEM_LIST)
                putString(EVENT_ACTION, PRODUCT_LIST_IMPRESSION_SHOP_DECOR)
                putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
                putString(EVENT_LABEL, eventLabel)
                putString(TRACKER_ID, TRACKER_ID_PRODUCT_LIST_IMPRESSION_SHOP_DECOR)
                putString(BUSINESS_UNIT, PHYSICAL_GOODS)
                putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
                putString(ITEM_LIST, itemList)
                putParcelableArrayList(
                    ITEMS,
                    arrayListOf(
                        createProductShopDecorItems(
                            horizontalPosition,
                            productId,
                            productName,
                            productDisplayedPrice,
                            widgetName
                        )
                    )
                )
                putString(PRODUCT_ID, productId)
                putString(SHOP_ID, shopId)
                putString(USER_ID, userId)
            }
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM_LIST, eventBundle)
        }
    }

    private fun createProductShopDecorItems(
        position: Int,
        productId: String,
        productName: String,
        productDisplayedPrice: String,
        itemListValue: String
    ): Bundle {
        return Bundle().apply {
            putString(DIMENSION_40, itemListValue)
            putInt(INDEX, position)
            putString(ITEM_BRAND, "")
            putString(ITEM_CATEGORY, "")
            putString(ITEM_ID, productId)
            putString(ITEM_NAME, productName)
            putString(ITEM_VARIANT, "")
            putString(PRICE, formatPrice(productDisplayedPrice))
        }
    }

    fun clickProduct(
        isLogin: Boolean,
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        shopName: String,
        horizontalPosition: Int,
        customDimensionShopPage: CustomDimensionShopPageAttribution,
        sortAndFilterValue: String,
        userId: String,
        selectedTabName: String
    ) {
        val etalaseChip = String.format(SELECTED_ETALASE_CHIP, ALL_PRODUCT)
        val loginNonLoginEventValue = if (isLogin) LOGIN else NON_LOGIN
        val listEventValue = joinDash(SHOPPAGE, customDimensionShopPage.shopId, etalaseChip, loginNonLoginEventValue, NOT_SEARCH_RESULT)
        val eventAction = joinDash(ALL_PRODUCT_CLICKED, etalaseChip, loginNonLoginEventValue, NOT_SEARCH_RESULT)
        val eventMap = createMap(
            PRODUCT_CLICK,
            SHOP_PAGE_BUYER,
            eventAction,
            "",
            customDimensionShopPage
        )
        eventMap[TRACKER_ID] = TRACKER_ID_ALL_PRODUCT_CLICKED
        eventMap[BUSINESS_UNIT] = PHYSICAL_GOODS
        eventMap[CURRENT_SITE] = TOKOPEDIA_MARKETPLACE
        eventMap[ECOMMERCE] = mutableMapOf(
            CLICK to mutableMapOf(
                ACTION_FIELD to mutableMapOf(LIST to listEventValue),
                PRODUCTS to mutableListOf(
                    createProductItemMap(
                        productName,
                        productId,
                        productDisplayedPrice,
                        shopName,
                        horizontalPosition,
                        customDimensionShopPage,
                        sortAndFilterValue,
                        listEventValue,
                        selectedTabName
                    )
                )
            )
        )
        eventMap[SHOP_ID] = customDimensionShopPage.shopId.orEmpty()
        eventMap[USER_ID] = userId
        sendDataLayerEvent(eventMap)
    }

    fun clickProductShopDecoration(trackerModel: ProductShopDecorationTrackerDataModel) {
        with(trackerModel){
            val cartTrackerValue = if (widgetOption == Int.ONE) WITH_CART else WITHOUT_CART
            var eventLabel = joinDash(
                HOME_TAB,
                verticalPosition.toString(),
                shopId,
                widgetName,
                widgetMasterId,
                cartTrackerValue
            )
            if (isFestivity) {
                eventLabel = joinDash(eventLabel, FESTIVITY)
            }
            val itemList = joinDash(SHOPPAGE, eventLabel)
            val eventBundle = Bundle().apply {
                putString(EVENT, SELECT_CONTENT)
                putString(EVENT_ACTION, CLICK_PRODUCT_SHOP_DECOR)
                putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
                putString(EVENT_LABEL, eventLabel)
                putString(TRACKER_ID, TRACKER_ID_CLICK_PRODUCT_SHOP_DECOR)
                putString(BUSINESS_UNIT, PHYSICAL_GOODS)
                putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
                putString(ITEM_LIST, itemList)
                putParcelableArrayList(
                    ITEMS,
                    arrayListOf(
                        createProductShopDecorItems(
                            horizontalPosition,
                            productId,
                            productName,
                            productDisplayedPrice,
                            widgetName
                        )
                    )
                )
                putString(PRODUCT_ID, productId)
                putString(SHOP_ID, shopId)
                putString(USER_ID, userId)
            }
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventBundle)
        }
    }

    fun clickProductPersonalization(
        isOwner: Boolean,
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        recommendationType: String,
        categoryBreadcrumbs: String,
        shopName: String,
        userId: String,
        horizontalPosition: Int,
        widgetHeaderTitle: String,
        widgetName: String,
        customDimensionShopPage: CustomDimensionShopPage
    ) {
        val widgetType = getShopPersoWidgetType(widgetName)
        val eventAction = joinDash(CLICK_PRODUCT_RECOMMENDATION, LOGIN)
        val eventLabel = joinDash(widgetHeaderTitle, widgetType)
        val actionFieldList = joinDash(
            SHOPPAGE,
            LOGIN,
            BUYER_RECOMMENDATION,
            recommendationType,
            widgetType
        )
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to PRODUCT_CLICK,
            EVENT_CATEGORY to getShopPageCategory(isOwner),
            EVENT_ACTION to eventAction,
            EVENT_LABEL to eventLabel,
            BUSINESS_UNIT to HOME_AND_BROWSE,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            PAGE_SOURCE to SCREEN_SHOP_PAGE,
            SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
            SHOP_TYPE to customDimensionShopPage.shopType.orEmpty(),
            USER_ID to userId
        )
        eventMap[ECOMMERCE] = mutableMapOf(
            CLICK to mutableMapOf(
                ACTION_FIELD to mutableMapOf(LIST to actionFieldList),
                PRODUCTS to mutableListOf(
                    createProductPersonalizationItemMap(
                        productName,
                        productId,
                        productDisplayedPrice,
                        shopName,
                        categoryBreadcrumbs,
                        horizontalPosition,
                        actionFieldList
                    )
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickProductPersonalizationReminder(
        isOwner: Boolean,
        isLogin: Boolean,
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        recommendationType: String,
        shopName: String,
        userId: String,
        horizontalPosition: Int,
        widgetHeaderTitle: String,
        customDimensionShopPage: CustomDimensionShopPage,
        categoryBreadcrumbs: String
    ) {
        val eventAction = CLICK_PRODUCT_RECOMMENDATION
        val loginNonLoginEventValue = if (isLogin) LOGIN else NON_LOGIN
        var eventLabel = joinDash(widgetHeaderTitle, WIDGET_TYPE_REMINDER)
        if (!isLogin) {
            eventLabel = joinDash(eventLabel, NON_LOGIN)
        }
        val actionFieldList = joinDash(
            SHOPPAGE,
            loginNonLoginEventValue,
            BUYER_RECOMMENDATION,
            recommendationType,
            WIDGET_TYPE_REMINDER
        )
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to PRODUCT_CLICK,
            EVENT_CATEGORY to getShopPageCategory(isOwner),
            EVENT_ACTION to eventAction,
            EVENT_LABEL to eventLabel,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            PAGE_TYPE to SHOPPAGE,
            SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
            SHOP_TYPE to customDimensionShopPage.shopType.orEmpty(),
            USER_ID to userId
        )
        eventMap[ECOMMERCE] = mutableMapOf(
            CLICK to mutableMapOf(
                ACTION_FIELD to mutableMapOf(LIST to actionFieldList),
                PRODUCTS to mutableListOf(
                    createProductPersonalizationItemMap(
                        productName,
                        productId,
                        productDisplayedPrice,
                        shopName,
                        categoryBreadcrumbs,
                        horizontalPosition,
                        actionFieldList
                    )
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    fun onImpressionShowcaseListWidgetItem(
        showcaseItem: ShopHomeShowcaseListItemUiModel,
        showcasePosition: Int,
        customDimensionShopPage: CustomDimensionShopPage,
        userId: String
    ) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to PROMO_VIEW,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_ACTION to IMPRESSION_ETALASE_NAVIGATION_BANNER,
            EVENT_LABEL to showcaseItem.viewType,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
            USER_ID to userId
        )
        eventMap[ECOMMERCE] = mutableMapOf(
            PROMO_VIEW to mutableMapOf(
                PROMOTIONS to mutableListOf(
                    mutableMapOf(
                        CREATIVE to showcaseItem.viewType,
                        ID to showcaseItem.id,
                        NAME to ETALASE_NAVIGATION_BANNER,
                        POSITION to showcasePosition
                    )
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickShowcaseListWidgetItem(
        showcaseItem: ShopHomeShowcaseListItemUiModel,
        showcasePosition: Int,
        customDimensionShopPage: CustomDimensionShopPage,
        userId: String
    ) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to PROMO_CLICK,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_ACTION to CLICK_ETALASE_NAVIGATION_BANNER,
            EVENT_LABEL to showcaseItem.viewType,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
            USER_ID to userId
        )
        eventMap[ECOMMERCE] = mutableMapOf(
            PROMO_CLICK to mutableMapOf(
                PROMOTIONS to mutableListOf(
                    mutableMapOf(
                        CREATIVE to showcaseItem.viewType,
                        ID to showcaseItem.id,
                        NAME to ETALASE_NAVIGATION_BANNER,
                        POSITION to showcasePosition
                    )
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    fun addToCart(
        isOwner: Boolean,
        cartId: String,
        attribution: String,
        isLogin: Boolean,
        layoutId: String,
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        productQuantity: Int,
        shopName: String,
        verticalPosition: Int,
        widgetId: String,
        widgetName: String,
        widgetOption: Int,
        customDimensionShopPage: CustomDimensionShopPage
    ) {
        val widgetNameEventValue = widgetName.takeIf { it.isNotEmpty() } ?: ALL_PRODUCT
        val eventAction = joinDash(CLICK_ADD_TO_CART, HOME_TAB, layoutId, widgetNameEventValue)
        val eventMap = createMap(
            ADD_TO_CART,
            getShopPageCategory(isOwner),
            eventAction,
            productId,
            customDimensionShopPage
        )
        eventMap[ECOMMERCE] = mutableMapOf(
            CURRENCY_CODE to IDR,
            ADD to mutableMapOf(
                PRODUCTS to mutableListOf(
                    createAddToCartProductItemMap(
                        productName,
                        productId,
                        productDisplayedPrice,
                        productQuantity,
                        shopName,
                        verticalPosition,
                        widgetId,
                        widgetNameEventValue,
                        widgetOption,
                        isLogin,
                        cartId,
                        attribution,
                        customDimensionShopPage
                    )
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    fun addToCartPersonalizationProduct(
        isOwner: Boolean,
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        productQuantity: Int,
        shopName: String,
        userId: String,
        widgetHeaderTitle: String,
        widgetName: String,
        customDimensionShopPage: CustomDimensionShopPage,
        recommendationType: String,
        categoryBreadcrumbs: String
    ) {
        val widgetType = getShopPersoWidgetType(widgetName)
        val eventLabel = joinDash(widgetHeaderTitle, widgetType)
        val eventAction = when (widgetType) {
            WIDGET_TYPE_CAROUSELL, WIDGET_TYPE_ADD_ONS -> CLICK_ATC_RECOMMENDATION
            WIDGET_TYPE_BUY_AGAIN -> CLICK_OCC_RECOMMENDATION
            else -> ""
        }
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to ADD_TO_CART,
            EVENT_CATEGORY to getShopPageCategory(isOwner),
            EVENT_ACTION to eventAction,
            EVENT_LABEL to eventLabel,
            BUSINESS_UNIT to HOME_AND_BROWSE,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            USER_ID to userId
        )
        eventMap[ECOMMERCE] = mutableMapOf(
            CURRENCY_CODE to IDR,
            ADD to mutableMapOf(
                PRODUCTS to mutableListOf(
                    createAddToCartProductPersonalizationItemMap(
                        productName,
                        productId,
                        productDisplayedPrice,
                        productQuantity,
                        customDimensionShopPage.shopId.orEmpty(),
                        shopName,
                        customDimensionShopPage.shopType.orEmpty(),
                        recommendationType,
                        categoryBreadcrumbs
                    )
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    fun addToCartPersonalizationProductReminder(
        isOwner: Boolean,
        isLogin: Boolean,
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        productQuantity: Int,
        shopName: String,
        userId: String,
        widgetHeaderTitle: String,
        cartId: String,
        recommendationType: String,
        customDimensionShopPage: CustomDimensionShopPage
    ) {
        val eventAction = CLICK_ATC_RECOMMENDATION
        var eventLabel = joinDash(widgetHeaderTitle, WIDGET_TYPE_REMINDER)
        if (!isLogin) {
            eventLabel = joinDash(eventLabel, NON_LOGIN)
        }
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to ADD_TO_CART,
            EVENT_CATEGORY to getShopPageCategory(isOwner),
            EVENT_ACTION to eventAction,
            EVENT_LABEL to eventLabel,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            USER_ID to userId,
            PAGE_TYPE to SHOPPAGE,
            SHOP_TYPE to customDimensionShopPage.shopType.orEmpty()
        )
        val loginNonLoginEventValue = if (isLogin) LOGIN else NON_LOGIN
        val actionFieldList = joinDash(
            SHOPPAGE,
            loginNonLoginEventValue,
            BUYER_RECOMMENDATION,
            recommendationType,
            WIDGET_TYPE_REMINDER
        )
        eventMap[ECOMMERCE] = mutableMapOf(
            CURRENCY_CODE to IDR,
            ADD to mutableMapOf(
                PRODUCTS to mutableListOf(
                    createAddToCartProductPersonalizationReminderItemMap(
                        productName,
                        productId,
                        productDisplayedPrice,
                        productQuantity,
                        customDimensionShopPage.shopId.orEmpty(),
                        shopName,
                        customDimensionShopPage.shopType.orEmpty(),
                        cartId,
                        actionFieldList
                    )
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    fun clickWishlist(
        isOwner: Boolean,
        isWishlist: Boolean,
        layoutId: String,
        isLogin: Boolean,
        widgetName: String,
        widgetId: String,
        productId: String,
        customDimensionShopPage: CustomDimensionShopPageProduct
    ) {
        val addOrRemoveEventValue = if (isWishlist) ADD else REMOVE
        val loginNonLoginEventValue = if (isLogin) LOGIN else NON_LOGIN
        val widgetNameEventValue = widgetName.takeIf { it.isNotEmpty() } ?: ALL_PRODUCT
        val widgetIdEventValue = widgetId.takeIf { it.isNotEmpty() } ?: ALL_PRODUCT
        sendGeneralEvent(
            CLICK_WISHLIST,
            getShopPageCategory(isOwner),
            "$addOrRemoveEventValue $WISHLIST - $layoutId - $widgetNameEventValue - $loginNonLoginEventValue",
            "$widgetIdEventValue - $GENERAL - $productId",
            customDimensionShopPage
        )
    }

    fun clickCta(trackerModel: ShopHomeCarouselProductWidgetClickCtaTrackerModel) {
        with(trackerModel) {
            var eventLabel = widgetMasterId
            if (isFestivity) {
                eventLabel = joinDash(eventLabel, FESTIVITY)
            }
            val eventMap: MutableMap<String, Any> = mutableMapOf(
                EVENT to CLICK_PG,
                EVENT_ACTION to CLICK_CTA_SEE_ALL,
                EVENT_CATEGORY to SHOP_PAGE_BUYER,
                EVENT_LABEL to eventLabel,
                TRACKER_ID to TRACKER_ID_PRODUCT_CAROUSEL_CLICK_CTA_SEE_ALL,
                BUSINESS_UNIT to PHYSICAL_GOODS,
                CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
                SHOP_ID to shopId,
                USER_ID to userId
            )
            TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
        }
    }

    private fun createDisplayWidgetPromotionsItemMap(
        widgetId: String,
        verticalPosition: Int,
        widgetName: String,
        widgetOption: String,
        destinationLink: String,
        assetUrl: String,
        horizontalPosition: Int,
        shopType: String,
        shopId: String
    ): Map<String, Any> {
        val nameEvent = joinDash(
            joinSpace(SHOPPAGE, HOME_DISPLAY_WIDGET),
            String.format(VERTICAL_POSITION, verticalPosition),
            widgetName,
            widgetOption

        )
        return mutableMapOf(
            ID to widgetId,
            NAME to nameEvent,
            CREATIVE to destinationLink,
            CREATIVE_URL to assetUrl,
            POSITION to horizontalPosition,
            DIMENSION_81 to shopType,
            DIMENSION_79 to shopId
        )
    }

    private fun createVoucherItemMap(
        parentPosition: Int,
        position: Int,
        voucherData: MerchantVoucherViewModel
    ): Map<String, Any> {
        val parentPositionEventValue = String.format(VERTICAL_POSITION, parentPosition)
        return mutableMapOf(
            ID to voucherData.voucherId,
            NAME to "$SHOPPAGE $HOME_TAB $MERCHANT_VOUCHER - $parentPositionEventValue",
            CREATIVE to (voucherData.voucherName ?: ""),
            CREATIVE_URL to (voucherData.bannerUrl ?: ""),
            POSITION to position,
            PROMO_ID to voucherData.voucherId,
            PROMO_CODE to voucherData.voucherCode
        )
    }

    private fun createProductPersonalizationItemMap(
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        shopName: String,
        categoryBreadcrumbs: String,
        horizontalPosition: Int,
        actionList: String
    ): Map<String, Any> {
        return mutableMapOf(
            NAME to productName,
            ID to productId,
            PRICE to formatPrice(productDisplayedPrice),
            BRAND to shopName,
            CATEGORY to categoryBreadcrumbs,
            VARIANT to NONE,
            LIST to actionList,
            POSITION to horizontalPosition
        )
    }

    private fun createProductItemMap(
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        shopName: String,
        horizontalPosition: Int,
        customDimensionShopPage: CustomDimensionShopPageAttribution,
        sortAndFilterValue: String,
        listEventValue: String,
        selectedTabName: String
    ): Map<String, Any> {
        val boe = if (customDimensionShopPage.isFulfillmentExist == true && customDimensionShopPage.isFreeOngkirActive == true) {
            BOE
        } else if (customDimensionShopPage.isFulfillmentExist != true && customDimensionShopPage.isFreeOngkirActive == true) {
            BO_PRODUCT
        } else {
            NON_BO_PRODUCT
        }
        return mutableMapOf(
            NAME to productName,
            ID to productId,
            PRICE to formatPrice(productDisplayedPrice),
            BRAND to shopName,
            CATEGORY to NONE,
            VARIANT to selectedTabName,
            LIST to listEventValue,
            POSITION to horizontalPosition,
            DIMENSION_81 to customDimensionShopPage.shopType.orEmpty(),
            DIMENSION_79 to customDimensionShopPage.shopId.orEmpty(),
            DIMENSION_90 to customDimensionShopPage.shopRef.orEmpty(),
            DIMENSION_83 to boe
        ).apply {
            if (sortAndFilterValue.isNotEmpty()) {
                put(DIMENSION_61, sortAndFilterValue)
            }
        }
    }

    private fun createProductListValue(
        isLogin: Boolean,
        verticalPosition: Int,
        widgetId: String,
        widgetNameEventValue: String,
        widgetOption: Int,
        customDimensionShopPage: CustomDimensionShopPage
    ): String {
        val featuredOrAllProductString = if (widgetId.isEmpty()) ALL_PRODUCT else HOME_FEATURED_PRODUCT
        val widgetIdEventValue = widgetId.takeIf { it.isNotEmpty() } ?: ALL_PRODUCT
        val widgetOptionEventValue = if (widgetOption == 1) WITH_CART else WITHOUT_CART
        val loginNonLoginEventValue = if (isLogin) LOGIN else NON_LOGIN
        return joinDash(
            joinSpace(
                SHOPPAGE,
                HOME_TAB,
                String.format(VERTICAL_POSITION, verticalPosition),
                featuredOrAllProductString
            ),
            widgetIdEventValue,
            customDimensionShopPage.shopId,
            widgetNameEventValue,
            widgetOptionEventValue,
            loginNonLoginEventValue
        )
    }

    private fun createAddToCartProductPersonalizationItemMap(
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        productQuantity: Int,
        shopId: String,
        shopName: String,
        shopType: String,
        recommendationType: String,
        categoryBreadcrumbs: String
    ): Map<String, Any> {
        return mutableMapOf(
            NAME to productName,
            ID to productId,
            PRICE to formatPrice(productDisplayedPrice),
            BRAND to shopName,
            CATEGORY to categoryBreadcrumbs,
            CATEGORY_ID to recommendationType,
            VARIANT to NONE,
            QUANTITY to productQuantity,
            DIMENSION_45 to "45",
            SHOP_ID to shopId,
            SHOP_NAME to shopName,
            SHOP_TYPE to shopType
        )
    }

    private fun createAddToCartProductPersonalizationReminderItemMap(
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        productQuantity: Int,
        shopId: String,
        shopName: String,
        shopType: String,
        cartId: String,
        actionFieldList: String
    ): Map<String, Any> {
        return mutableMapOf(
            NAME to productName,
            ID to productId,
            PRICE to formatPrice(productDisplayedPrice),
            BRAND to shopName,
            CATEGORY to NONE,
            CATEGORY_ID to NONE,
            VARIANT to NONE,
            QUANTITY to productQuantity,
            DIMENSION_45 to cartId,
            DIMENSION_40 to actionFieldList,
            SHOP_ID to shopId,
            SHOP_NAME to shopName,
            SHOP_TYPE to shopType
        )
    }

    private fun createAddToCartProductItemMap(
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        productQuantity: Int,
        shopName: String,
        verticalPosition: Int,
        widgetId: String,
        widgetNameEventValue: String,
        widgetOption: Int,
        isLogin: Boolean,
        cartId: String,
        attribution: String,
        customDimensionShopPage: CustomDimensionShopPage
    ): Map<String, Any> {
        val dimension40Value = createProductListValue(
            isLogin,
            verticalPosition,
            widgetId,
            widgetNameEventValue,
            widgetOption,
            customDimensionShopPage
        )
        return mutableMapOf(
            NAME to productName,
            ID to productId,
            PRICE to formatPrice(productDisplayedPrice),
            BRAND to shopName,
            CATEGORY to NONE,
            VARIANT to NONE,
            QUANTITY to productQuantity,
            DIMENSION_80 to shopName,
            DIMENSION_82 to "",
            DIMENSION_45 to cartId,
            DIMENSION_38 to attribution,
            DIMENSION_40 to dimension40Value,
            DIMENSION_81 to customDimensionShopPage.shopType.orEmpty(),
            DIMENSION_79 to customDimensionShopPage.shopId.orEmpty()
        )
    }

    fun clickMoreMenuChip(
        isOwner: Boolean,
        selectedEtalaseName: String,
        customDimensionShopPage: CustomDimensionShopPage
    ) {
        sendGeneralEvent(
            CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner),
            CLICK_SHOWCASE_LIST,
            String.format(ETALASE_X, selectedEtalaseName),
            customDimensionShopPage
        )
    }

    fun clickClearFilter(isOwner: Boolean, customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(
            CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner),
            CLICK_CLOSE_FILTER,
            "",
            customDimensionShopPage
        )
    }

    fun impressionCampaignNplWidget(
        statusCampaign: String,
        shopId: String,
        position: Int,
        isSeeCampaign: Boolean?,
        imageId: String,
        imageUrl: String,
        customDimensionShopPage: CustomDimensionShopPage,
        isOwner: Boolean
    ) {
        val trackerBannerType = getCampaignNplTrackerBannerType(statusCampaign)
        var eventLabel = joinDash(trackerBannerType, shopId, position.toString())
        isSeeCampaign?.let {
            eventLabel = if (it) {
                joinDash(eventLabel, VALUE_SEE_CAMPAIGN)
            } else {
                joinDash(eventLabel, VALUE_NO_SEE_CAMPAIGN)
            }
        }
        val eventMap = createMap(
            PROMO_VIEW,
            getShopPageCategory(isOwner),
            IMPRESSION,
            eventLabel,
            customDimensionShopPage
        )
        eventMap[ECOMMERCE] = mutableMapOf(
            PROMO_VIEW to mutableMapOf(
                PROMOTIONS to mutableListOf(
                    createCampaignNplWidgetItemMap(
                        imageId,
                        trackerBannerType,
                        position,
                        imageUrl
                    )
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    fun impressionShopHomeCampaignWidget(
        trackerModel: ShopHomeCampaignWidgetImpressionTrackerModel
    ) {
        with(trackerModel) {
            var eventLabel = joinDash(
                shopId,
                campaignId,
                campaignName,
                statusCampaign,
                widgetMasterId
            )
            if (isFestivity) {
                eventLabel = joinDash(eventLabel, FESTIVITY)
            }
            val eventBundle = Bundle().apply {
                putString(EVENT, VIEW_ITEM)
                putString(EVENT_ACTION, CAMPAIGN_WIDGET_IMPRESSION)
                putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
                putString(EVENT_LABEL, eventLabel)
                putString(TRACKER_ID, TRACKER_ID_CAMPAIGN_WIDGET_IMPRESSION)
                putString(BUSINESS_UNIT, PHYSICAL_GOODS)
                putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
                putParcelableArrayList(
                    PROMOTIONS,
                    arrayListOf(
                        createCampaignWidgetPromotionItem(
                            position,
                            campaignId,
                            campaignName
                        )
                    )
                )
                putString(SHOP_ID, shopId)
                putString(USER_ID, userId)
            }
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM, eventBundle)
        }
    }

    private fun createCampaignWidgetPromotionItem(position: Int, campaignId: String, campaignName: String): Bundle {
        return Bundle().apply {
            putString(CREATIVE_NAME, "")
            putString(CREATIVE_SLOT, position.toString())
            putString(ITEM_ID, campaignId)
            putString(ITEM_NAME, campaignName)
        }
    }

    fun onClickTnCButtonFlashSaleWidget(
        campaignId: String,
        shopId: String,
        userId: String,
        isOwner: Boolean
    ) {
        val eventMap = createFlashSaleTrackerMap(
            eventName = CLICK_SHOP_PAGE,
            eventAction = joinSpace(CLICK, FLASH_SALE),
            eventCategory = getShopPageCategory(isOwner),
            eventLabel = joinDash(CLICK_TNC_BUTTON_FLASH_SALE_WIDGET, campaignId),
            shopId = shopId,
            userId = userId
        )
        sendDataLayerEvent(eventMap)
    }

    private fun createFlashSaleTrackerMap(
        eventName: String,
        eventAction: String,
        eventCategory: String,
        eventLabel: String,
        shopId: String,
        userId: String,
        ecommerceMap: Map<String, Any>? = null
    ): MutableMap<String, Any> {
        val flashSaleEventMap = mutableMapOf<String, Any>(
            EVENT to eventName,
            EVENT_ACTION to eventAction,
            EVENT_CATEGORY to eventCategory,
            EVENT_LABEL to eventLabel,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to shopId,
            USER_ID to userId
        )
        ecommerceMap?.let {
            flashSaleEventMap[ECOMMERCE] = it
        }

        return flashSaleEventMap
    }

    private fun createEcommerceFlashSaleItemMap(
        creative: String,
        id: String,
        name: String,
        position: Int
    ): Map<String, Any> {
        return mapOf(
            CREATIVE to creative,
            ID to id,
            NAME to name,
            POSITION to (position + 1)
        )
    }

    private fun createCampaignNplWidgetItemMap(
        imageId: String,
        trackerBannerType: String,
        position: Int,
        imageUrl: String
    ): Map<String, Any> {
        val nameEvent = joinDash(
            joinSpace(SHOPPAGE, VALUE_HOME, trackerBannerType),
            position.toString()
        )
        return mutableMapOf(
            ID to imageId,
            NAME to nameEvent,
            CREATIVE to "",
            CREATIVE_URL to imageUrl,
            POSITION to position
        )
    }

    fun clickTncButton(
        isOwner: Boolean,
        statusCampaign: String,
        customDimensionShopPage: CustomDimensionShopPage
    ) {
        val trackerCampaignType = getCampaignNplTrackerCampaignType(statusCampaign)
        sendGeneralEvent(
            CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner),
            String.format(CLICK_TNC, trackerCampaignType),
            "",
            customDimensionShopPage
        )
    }

    private fun getCampaignNplTrackerBannerType(statusCampaign: String): String {
        return when (statusCampaign.toLowerCase()) {
            StatusCampaign.UPCOMING.statusCampaign.toLowerCase() -> {
                VALUE_UPCOMING_BANNER
            }
            StatusCampaign.ONGOING.statusCampaign.toLowerCase() -> {
                VALUE_ONGOING_BANNER
            }
            else -> {
                VALUE_FINISHED_BANNER
            }
        }
    }

    private fun getCampaignNplTrackerCampaignType(statusCampaign: String): String {
        return when (statusCampaign.toLowerCase()) {
            StatusCampaign.UPCOMING.statusCampaign.toLowerCase() -> {
                VALUE_UPCOMING_CAMPAIGN
            }
            StatusCampaign.ONGOING.statusCampaign.toLowerCase() -> {
                VALUE_ONGOING_CAMPAIGN
            }
            else -> {
                VALUE_FINISHED_CAMPAIGN
            }
        }
    }

    fun impressionTncPage(
        isOwner: Boolean,
        statusCampaign: String,
        customDimensionShopPage: CustomDimensionShopPage
    ) {
        val trackerCampaignType = getCampaignNplTrackerCampaignType(statusCampaign)
        sendGeneralEvent(
            VIEW_SHOP_PAGE_IRIS,
            getShopPageCategory(isOwner),
            String.format(IMPRESSION_TNC, trackerCampaignType),
            "",
            customDimensionShopPage
        )
    }

    fun clickCloseTncPage(
        isOwner: Boolean,
        statusCampaign: String,
        customDimensionShopPage: CustomDimensionShopPage
    ) {
        val trackerCampaignType = getCampaignNplTrackerCampaignType(statusCampaign)
        sendGeneralEvent(
            CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner),
            String.format(CLICK_CLOSE_TNC, trackerCampaignType),
            "",
            customDimensionShopPage
        )
    }

    fun clickNotifyMeButton(
        isOwner: Boolean,
        action: String,
        customDimensionShopPage: CustomDimensionShopPage
    ) {
        val eventAction = if (action.toLowerCase() == NotifyMeAction.REGISTER.action.toLowerCase()) {
            CLICK_ACTIVATE_REMINDER
        } else {
            CLICK_DEACTIVATE_REMINDER
        }
        sendGeneralEvent(
            CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner),
            eventAction,
            "",
            customDimensionShopPage
        )
    }

    fun impressionToasterActivation(isOwner: Boolean, customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(
            VIEW_SHOP_PAGE_IRIS,
            getShopPageCategory(isOwner),
            IMPRESSION_TOASTER_NOTIFY_ME,
            "",
            customDimensionShopPage
        )
    }

    fun toasterActivationClickOk(isOwner: Boolean, customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(
            CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner),
            CLICK_OK_TOASTER_NOTIFY_ME,
            "",
            customDimensionShopPage
        )
    }

    fun clickCtaSeeAllCampaignWidget(
        trackerModel: ShopHomeCampaignWidgetClickCtaSeeAllTrackerModel
    ) {
        with(trackerModel) {
            var eventLabel = joinDash(
                shopId,
                campaignId,
                campaignName,
                statusCampaign,
                widgetMasterId
            )
            if (isFestivity) {
                eventLabel = joinDash(eventLabel, FESTIVITY)
            }
            val eventMap: MutableMap<String, Any> = mutableMapOf(
                EVENT to CLICK_PG,
                EVENT_ACTION to CAMPAIGN_WIDGET_CLICK_CTA_SEE_ALL,
                EVENT_CATEGORY to SHOP_PAGE_BUYER,
                EVENT_LABEL to eventLabel,
                TRACKER_ID to TRACKER_ID_CAMPAIGN_WIDGET_CLICK_CTA_SEE_ALL,
                BUSINESS_UNIT to PHYSICAL_GOODS,
                CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
                SHOP_ID to shopId,
                USER_ID to userId
            )
            TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
        }
    }

    fun clickReminderCampaignWidget(
        trackerModel: ShopHomeCampaignWidgetClickReminderTrackerModel
    ) {
        with(trackerModel) {
            var eventLabel = joinDash(
                shopId,
                campaignId,
                campaignName,
                statusCampaign,
                widgetMasterId
            )
            if (isFestivity) {
                eventLabel = joinDash(eventLabel, FESTIVITY)
            }
            val eventMap: MutableMap<String, Any> = mutableMapOf(
                EVENT to CLICK_PG,
                EVENT_ACTION to CAMPAIGN_WIDGET_CLICK_REMINDER,
                EVENT_CATEGORY to SHOP_PAGE_BUYER,
                EVENT_LABEL to eventLabel,
                TRACKER_ID to TRACKER_ID_CAMPAIGN_WIDGET_CLICK_REMINDER,
                BUSINESS_UNIT to PHYSICAL_GOODS,
                CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
                SHOP_ID to shopId,
                USER_ID to userId
            )
            TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
        }
    }

    fun clickBannerWidgetCampaign(
        trackerModel: ShopHomeCampaignWidgetClickBannerTrackerModel
    ) {
        with(trackerModel) {
            var eventLabel = joinDash(
                shopId,
                campaignId,
                campaignName,
                statusCampaign,
                widgetMasterId
            )
            if (isFestivity) {
                eventLabel = joinDash(eventLabel, FESTIVITY)
            }
            val eventBundle = Bundle().apply {
                putString(EVENT, SELECT_CONTENT)
                putString(EVENT_ACTION, CAMPAIGN_WIDGET_CLICK_BANNER)
                putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
                putString(EVENT_LABEL, eventLabel)
                putString(TRACKER_ID, TRACKER_ID_CAMPAIGN_WIDGET_CLICK_BANNER)
                putString(BUSINESS_UNIT, PHYSICAL_GOODS)
                putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
                putParcelableArrayList(
                    PROMOTIONS,
                    arrayListOf(
                        createBannerPromotionsItem(
                            position,
                        )
                    )
                )
                putString(SHOP_ID, shopId)
                putString(USER_ID, userId)
            }
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventBundle)
        }
    }

    private fun createBannerPromotionsItem(position: Int): Bundle {
        return Bundle().apply {
            putString(CREATIVE_NAME, "")
            putInt(CREATIVE_SLOT, position)
            putString(ITEM_ID, "")
            putString(ITEM_NAME, "")
        }
    }

    fun onImpressionCampaignWidgetProduct(
        trackerModel: ShopHomeCampaignWidgetProductTrackerModel
    ) {
        with(trackerModel) {
            var eventLabel = joinDash(
                shopId,
                campaignId,
                campaignName,
                statusCampaign,
                widgetMasterId
            )
            var itemList = joinDash("$SHOPPAGE $widgetPosition", campaignId, campaignName, statusCampaign, widgetMasterId)
            if (isFestivity) {
                eventLabel = joinDash(eventLabel, FESTIVITY)
                itemList = joinDash(itemList, FESTIVITY)
            }
            val eventBundle = Bundle().apply {
                putString(EVENT, VIEW_ITEM_LIST)
                putString(EVENT_ACTION, CAMPAIGN_WIDGET_PRODUCT_CARD_IMPRESSION)
                putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
                putString(EVENT_LABEL, eventLabel)
                putString(TRACKER_ID, TRACKER_ID_CAMPAIGN_WIDGET_PRODUCT_CARD_IMPRESSION)
                putString(BUSINESS_UNIT, PHYSICAL_GOODS)
                putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
                putString(ITEM_LIST, itemList)
                putParcelableArrayList(
                    ITEMS,
                    arrayListOf(
                        createCampaignProductItems(
                            position,
                            productId,
                            productName,
                            productPrice,
                            itemList
                        )
                    )
                )
                putString(PRODUCT_ID, productId)
                putString(SHOP_ID, shopId)
                putString(USER_ID, userId)
            }
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM_LIST, eventBundle)
        }
    }

    private fun createCampaignNplProductListValue(verticalPosition: Int, statusCampaign: String, shopId: String, isLogin: Boolean): String {
        val loginNonLoginEventValue = if (isLogin) LOGIN else NON_LOGIN
        val bannerTypeEventValue = getCampaignNplTrackerBannerType(statusCampaign)
        return joinDash(
            joinSpace(SHOPPAGE, VALUE_HOME, String.format(VERTICAL_POSITION, verticalPosition)),
            bannerTypeEventValue,
            shopId,
            loginNonLoginEventValue
        )
    }

    private fun createCampaignNplProductItemMap(
        productName: String,
        productId: String,
        productPrice: String,
        shopName: String,
        listEventValue: String,
        horizontalPosition: Int,
        customDimensionShopPage: CustomDimensionShopPage
    ): Map<String, Any> {
        return mutableMapOf(
            NAME to productName,
            ID to productId,
            PRICE to formatPrice(productPrice),
            BRAND to shopName,
            CATEGORY to NONE,
            VARIANT to NONE,
            LIST to listEventValue,
            POSITION to horizontalPosition,
            DIMENSION_81 to customDimensionShopPage.shopType.orEmpty(),
            DIMENSION_79 to customDimensionShopPage.shopId.orEmpty(),
            DIMENSION_40 to listEventValue
        )
    }

    fun clickCampaignWidgetProduct(
        trackerModel: ShopHomeCampaignWidgetProductTrackerModel
    ) {
        with(trackerModel) {
            var eventLabel = joinDash(
                shopId,
                campaignId,
                campaignName,
                statusCampaign,
                widgetMasterId
            )
            var itemList = joinDash("$SHOPPAGE $widgetPosition", campaignId, campaignName, statusCampaign, widgetMasterId)
            if (isFestivity) {
                eventLabel = joinDash(eventLabel, FESTIVITY)
                itemList = joinDash(itemList, FESTIVITY)
            }
            val eventBundle = Bundle().apply {
                putString(EVENT, SELECT_CONTENT)
                putString(EVENT_ACTION, CAMPAIGN_WIDGET_PRODUCT_CARD_CLICK)
                putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
                putString(EVENT_LABEL, eventLabel)
                putString(TRACKER_ID, TRACKER_ID_CAMPAIGN_WIDGET_PRODUCT_CARD_CLICK)
                putString(BUSINESS_UNIT, PHYSICAL_GOODS)
                putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
                putString(ITEM_LIST, itemList)
                putParcelableArrayList(
                    ITEMS,
                    arrayListOf(
                        createCampaignProductItems(
                            position,
                            productId,
                            productName,
                            productPrice,
                            itemList
                        )
                    )
                )
                putString(PRODUCT_ID, productId)
                putString(SHOP_ID, shopId)
                putString(USER_ID, userId)
            }
            TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventBundle)
        }
    }

    private fun createCampaignProductItems(
        position: Int,
        productId: String,
        productName: String,
        productDisplayedPrice: String,
        itemListValue: String
    ): Bundle {
        return Bundle().apply {
            putString(DIMENSION_40, itemListValue)
            putInt(INDEX, position)
            putString(ITEM_BRAND, "")
            putString(ITEM_CATEGORY, "")
            putString(ITEM_ID, productId)
            putString(ITEM_NAME, productName)
            putString(ITEM_VARIANT, "")
            putString(PRICE, formatPrice(productDisplayedPrice))
        }
    }

    fun clickProductListToggle(
        initialView: ShopProductViewGridType,
        finalView: ShopProductViewGridType,
        shopId: String,
        userId: String
    ) {
        val initialViewString = ShopUtil.getShopGridViewTypeString(initialView)
        val finalViewString = ShopUtil.getShopGridViewTypeString(finalView)
        val eventLabel = String.format(ShopPageTrackingConstant.LABEL_CLICK_PRODUCT_LIST_TOGGLE, initialViewString, finalViewString)
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to CLICK_SHOP_PAGE,
            EVENT_ACTION to ACTION_CLICK_PRODUCT_LIST_TOGGLE,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to eventLabel,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to shopId,
            USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun clickFilterChips(productListName: String, customDimensionShopPage: CustomDimensionShopPage) {
        sendGeneralEvent(
            CLICK_SHOP_PAGE,
            SHOP_PAGE_BUYER,
            CLICK_FILTER_CHIP,
            productListName,
            customDimensionShopPage
        )
    }

    fun clickApplyFilter(
        selectedSortName: String,
        selectedFilterMap: Map<String, String>,
        shopId: String,
        userId: String
    ) {
        var eventLabel = ShopPageTrackingConstant.LABEL_CLICK_APPLY_FILTER_CHIP
        if (selectedSortName.isNotBlank()) {
            eventLabel += " - $selectedSortName"
        }
        if (!selectedFilterMap[PMAX_PARAM_KEY].isNullOrBlank() || !selectedFilterMap[PMIN_PARAM_KEY].isNullOrBlank()) {
            val minPrice = selectedFilterMap[PMIN_PARAM_KEY] ?: VALUE_STRING_ZERO
            val maxPrice = selectedFilterMap[PMAX_PARAM_KEY] ?: VALUE_STRING_ZERO
            eventLabel += " - $minPrice - $maxPrice"
        }
        if (!selectedFilterMap[RATING_PARAM_KEY].isNullOrBlank()) {
            val rating = selectedFilterMap[RATING_PARAM_KEY] ?: VALUE_STRING_ZERO
            eventLabel += " - $rating"
        }
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to CLICK_SHOP_PAGE,
            EVENT_ACTION to CLICK_FILTER_CHIP,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to eventLabel,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to shopId,
            USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun clickNotifyMeNplFollowerButton(isOwner: Boolean, action: String, userId: String, customDimensionShopPage: CustomDimensionShopPage) {
        val eventAction = if (action.toLowerCase() == NotifyMeAction.REGISTER.action.toLowerCase()) {
            "$CLICK_ACTIVATE_REMINDER - $CAMPAIGN_SEGMENTATION"
        } else {
            "$CLICK_DEACTIVATE_REMINDER - $CAMPAIGN_SEGMENTATION"
        }
        sendGeneralEventNplFollower(
            CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner),
            eventAction,
            "",
            PHYSICAL_GOODS,
            TOKOPEDIA_MARKETPLACE,
            userId,
            customDimensionShopPage
        )
    }

    fun clickTncBottomSheetFollowButtonNplFollower(
        isOwner: Boolean,
        isFollowShop: Boolean,
        shopId: String,
        userId: String,
        customDimensionShopPage: CustomDimensionShopPage
    ) {
        val eventAction = String.format(
            CLICK_FOLLOW_UNFOLLOW_TNC_PAGE,
            FOLLOW.takeIf { isFollowShop }
                ?: UNFOLLOW
        )
        sendGeneralEventNplFollower(
            CLICK_SHOP_PAGE,
            getShopPageCategory(isOwner),
            eventAction,
            shopId,
            PHYSICAL_GOODS,
            TOKOPEDIA_MARKETPLACE,
            userId,
            customDimensionShopPage
        )
    }

    fun impressionCarouselProductShowcaseItem(
        isOwner: Boolean,
        isLogin: Boolean,
        etalaseId: String,
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        shopName: String,
        verticalPosition: Int,
        horizontalPosition: Int,
        etalaseName: String,
        userId: String,
        customDimensionShopPage: CustomDimensionShopPageAttribution
    ) {
        val eventAction = joinDash(PRODUCT_LIST_IMPRESSION, HOME_TAB, etalaseId, ETALASE_WIDGET)
        val loginNonLoginEventValue = if (isLogin) LOGIN else NON_LOGIN
        val actionFieldList = joinDash(
            joinSpace(
                SHOPPAGE,
                HOME_TAB,
                String.format(VERTICAL_POSITION, verticalPosition),
                ETALASE_WIDGET
            ),
            etalaseId,
            customDimensionShopPage.shopId,
            etalaseName,
            loginNonLoginEventValue
        )
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to PRODUCT_VIEW,
            EVENT_CATEGORY to getShopPageCategory(isOwner),
            EVENT_ACTION to eventAction,
            EVENT_LABEL to "",
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            PAGE_TYPE to SHOPPAGE,
            USER_ID to userId
        )
        eventMap[ECOMMERCE] = mutableMapOf(
            CURRENCY_CODE to IDR,
            IMPRESSIONS to mutableListOf(
                createProductShowcaseItemMap(
                    productName,
                    productId,
                    productDisplayedPrice,
                    shopName,
                    horizontalPosition,
                    actionFieldList,
                    customDimensionShopPage
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    private fun createProductShowcaseItemMap(
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        shopName: String,
        horizontalPosition: Int,
        actionFieldList: String,
        customDimensionShopPage: CustomDimensionShopPageAttribution
    ): Any {
        val boe = if (customDimensionShopPage.isFulfillmentExist == true && customDimensionShopPage.isFreeOngkirActive == true) {
            BOE
        } else if (customDimensionShopPage.isFulfillmentExist != true && customDimensionShopPage.isFreeOngkirActive == true) {
            BO_PRODUCT
        } else {
            NON_BO_PRODUCT
        }
        return mutableMapOf(
            NAME to productName,
            ID to productId,
            PRICE to formatPrice(productDisplayedPrice),
            BRAND to shopName,
            CATEGORY to NONE,
            VARIANT to NONE,
            LIST to actionFieldList,
            POSITION to horizontalPosition,
            DIMENSION_81 to customDimensionShopPage.shopType.orEmpty(),
            DIMENSION_79 to customDimensionShopPage.shopId.orEmpty(),
            DIMENSION_83 to boe
        )
    }

    fun clickCarouselProductShowcaseItem(
        isOwner: Boolean,
        isLogin: Boolean,
        etalaseId: String,
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        shopName: String,
        verticalPosition: Int,
        horizontalPosition: Int,
        etalaseName: String,
        userId: String,
        customDimensionShopPage: CustomDimensionShopPageAttribution
    ) {
        val eventAction = joinDash(CLICK_PRODUCT, HOME_TAB, etalaseId, ETALASE_WIDGET)
        val loginNonLoginEventValue = if (isLogin) LOGIN else NON_LOGIN
        val actionFieldList = joinDash(
            joinSpace(
                SHOPPAGE,
                HOME_TAB,
                String.format(VERTICAL_POSITION, verticalPosition),
                ETALASE_WIDGET
            ),
            etalaseId,
            customDimensionShopPage.shopId,
            etalaseName,
            loginNonLoginEventValue
        )
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to PRODUCT_CLICK,
            EVENT_CATEGORY to getShopPageCategory(isOwner),
            EVENT_ACTION to eventAction,
            EVENT_LABEL to productId,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            PAGE_TYPE to SHOPPAGE,
            USER_ID to userId
        )
        eventMap[ECOMMERCE] = mutableMapOf(
            CLICK to mutableMapOf(
                ACTION_FIELD to mutableMapOf(LIST to actionFieldList),
                PRODUCTS to mutableListOf(
                    createProductShowcaseItemMap(
                        productName,
                        productId,
                        productDisplayedPrice,
                        shopName,
                        horizontalPosition,
                        actionFieldList,
                        customDimensionShopPage
                    )
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    fun addToCartCarouselProductShowcaseItem(
        isOwner: Boolean,
        isLogin: Boolean,
        etalaseId: String,
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        productQuantity: Int,
        shopName: String,
        verticalPosition: Int,
        etalaseName: String,
        userId: String,
        cartId: String,
        customDimensionShopPage: CustomDimensionShopPageAttribution
    ) {
        val eventAction = joinDash(CLICK_ADD_TO_CART, HOME_TAB, etalaseId, ETALASE_WIDGET)
        val loginNonLoginEventValue = if (isLogin) LOGIN else NON_LOGIN
        val actionFieldList = joinDash(
            joinSpace(
                SHOPPAGE,
                HOME_TAB,
                String.format(VERTICAL_POSITION, verticalPosition),
                ETALASE_WIDGET
            ),
            etalaseId,
            customDimensionShopPage.shopId,
            etalaseName,
            loginNonLoginEventValue
        )
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to ADD_TO_CART,
            EVENT_CATEGORY to getShopPageCategory(isOwner),
            EVENT_ACTION to eventAction,
            EVENT_LABEL to productId,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            PAGE_TYPE to SHOPPAGE,
            USER_ID to userId
        )
        eventMap[ECOMMERCE] = mutableMapOf(
            CURRENCY_CODE to IDR,
            ADD to mutableMapOf(
                PRODUCTS to mutableListOf(
                    createAddToCartCarouselProductShowcaseItemMap(
                        productName,
                        productId,
                        productDisplayedPrice,
                        productQuantity,
                        shopName,
                        cartId,
                        actionFieldList,
                        customDimensionShopPage
                    )
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    private fun createAddToCartCarouselProductShowcaseItemMap(
        productName: String,
        productId: String,
        productDisplayedPrice: String,
        productQuantity: Int,
        shopName: String,
        cartId: String,
        listValue: String,
        customDimensionShopPage: CustomDimensionShopPageAttribution
    ): Map<String, Any> {
        val boe = if (customDimensionShopPage.isFulfillmentExist == true && customDimensionShopPage.isFreeOngkirActive == true) {
            BOE
        } else if (customDimensionShopPage.isFulfillmentExist != true && customDimensionShopPage.isFreeOngkirActive == true) {
            BO_PRODUCT
        } else {
            NON_BO_PRODUCT
        }
        return mutableMapOf(
            NAME to productName,
            ID to productId,
            PRICE to formatPrice(productDisplayedPrice),
            BRAND to shopName,
            CATEGORY to NONE,
            CATEGORY_ID to NONE,
            VARIANT to NONE,
            QUANTITY to productQuantity,
            DIMENSION_45 to cartId,
            DIMENSION_40 to listValue,
            DIMENSION_83 to boe,
            SHOP_ID to customDimensionShopPage.shopId.orEmpty(),
            SHOP_NAME to shopName,
            SHOP_TYPE to customDimensionShopPage.shopType.orEmpty()
        )
    }

    fun onImpressionShopHomeWidget(
        segmentName: String,
        decorWidgetName: String,
        widgetId: String,
        widgetPosition: Int,
        shopId: String,
        userId: String,
        widgetMasterId: String,
        isFestivity: Boolean
    ) {
        var eventLabel = joinDash(
            LABEL_SHOP_DECOR_IMPRESSION,
            segmentName,
            decorWidgetName,
            widgetMasterId
        )
        eventLabel = if (isFestivity) {
            joinDash(eventLabel, FESTIVITY)
        } else {
            eventLabel
        }
        val eventBundle = Bundle().apply {
            putString(EVENT, VIEW_ITEM)
            putString(EVENT_ACTION, ACTION_SHOP_DECOR_IMPRESSION)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, eventLabel)
            putString(TRACKER_ID, TRACKER_ID_IMPRESSION_SHOP_DECOR)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putParcelableArrayList(
                PROMOTIONS,
                arrayListOf(
                    Bundle().apply {
                        putString(CREATIVE_NAME, decorWidgetName)
                        putInt(CREATIVE_SLOT, widgetPosition)
                        putString(ITEM_ID, widgetId)
                        putString(ITEM_NAME, VALUE_SHOP_DECOR)
                    }
                )
            )
            putString(SHOP_ID, shopId)
            putString(USER_ID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM, eventBundle)
    }

    fun onClickedShopHomeWidget(
        segmentName: String,
        decorWidgetName: String,
        widgetId: String,
        widgetPosition: Int,
        shopId: String,
        userId: String,
        widgetMasterId: String,
        isFestivity: Boolean
    ) {
        var eventLabel = joinDash(
            LABEL_SHOP_DECOR_CLICK,
            segmentName,
            decorWidgetName,
            widgetMasterId
        )
        eventLabel = if (isFestivity) {
            joinDash(eventLabel, FESTIVITY)
        } else {
            eventLabel
        }
        val eventBundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_ACTION, ACTION_SHOP_DECOR_CLICK)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, eventLabel)
            putString(TRACKER_ID, TRACKER_ID_CLICK_SHOP_DECOR)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putParcelableArrayList(
                PROMOTIONS,
                arrayListOf(
                    Bundle().apply {
                        putString(CREATIVE_NAME, decorWidgetName)
                        putInt(CREATIVE_SLOT, widgetPosition)
                        putString(ITEM_ID, widgetId)
                        putString(ITEM_NAME, VALUE_SHOP_DECOR)
                    }
                )
            )
            putString(SHOP_ID, shopId)
            putString(USER_ID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventBundle)
    }

    fun sendUserViewHomeTabWidgetTracker(
        masterLayoutId: String,
        shopId: String
    ) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to VIEW_SHOP_PAGE_IRIS,
            EVENT_ACTION to ACTION_HOME_TAB_IMPRESSION,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to masterLayoutId,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to shopId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun impressionCardDonationWidget(
        isOwner: Boolean,
        shopId: String
    ) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to VIEW_DIGITAL_IRIS,
            EVENT_ACTION to IMPRESSION_DONATION_BY_SELLER,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to shopId,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun actionClickCardDonationWidget(
        isOwner: Boolean,
        shopId: String
    ) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to CLICK_DIGITAL,
            EVENT_ACTION to CLICK_DONATION_BY_SELLER,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to shopId,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun actionPressBackDonation(
        isOwner: Boolean,
        shopId: String
    ) {
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to CLICK_DIGITAL,
            EVENT_ACTION to CLICK_BACK_BUTTON,
            EVENT_CATEGORY to SHOP_PAGE_DONATION_BY_SELLER,
            EVENT_LABEL to shopId,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    /**
     * Product bundling widget tracker
     */
    fun clickAtcProductBundleMultiple(
        atcBundleModel: AddToCartBundleModel,
        bundleName: String,
        bundleId: String,
        bundlePrice: String,
        quantity: String,
        shopName: String,
        shopType: String,
        bundlePriceCut: String,
        shopId: String,
        userId: String,
        isFestivity: Boolean
    ) {
        val bundle = Bundle()
        val itemBundle = arrayListOf<Bundle>()
        val eventLabel = if(isFestivity){
            joinDash(bundleId, bundleName, bundlePriceCut, FESTIVITY)
        } else {
            joinDash(bundleId, bundleName, bundlePriceCut)
        }
        atcBundleModel.addToCartBundleDataModel.data.forEachIndexed { position, productDataModel ->
            itemBundle.add(
                getItemsBundlingAtc(
                    bundleId = bundleId,
                    cartId = productDataModel.cartId,
                    bundleName = bundleName,
                    bundlePrice = formatPrice(bundlePrice),
                    quantity = quantity,
                    shopName = shopName,
                    shopId = productDataModel.shopId,
                    shopType = shopType,
                    bundleType = VALUE_MULTIPLE_BUNDLING
                )
            )
        }
        bundle.putString(TrackAppUtils.EVENT, BUNDLING_ADD_TO_CART)
        bundle.putString(TrackAppUtils.EVENT_ACTION, joinDash(CLICK, MULTIPLE_BUNDLE_WIDGET, BUNDLE_ADD_TO_CART))
        bundle.putString(TrackAppUtils.EVENT_CATEGORY, SHOP_PAGE_BUYER)
        bundle.putString(TrackAppUtils.EVENT_LABEL, eventLabel)
        bundle.putString(TRACKER_ID, TRACKER_ID_ATC_MULTIPLE_BUNDLING_WDIGET)
        bundle.putString(BUSINESS_UNIT, PHYSICAL_GOODS)
        bundle.putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
        bundle.putString(SHOP_ID, userId)
        bundle.putString(USER_ID, shopId)
        bundle.putParcelableArrayList(ITEMS, itemBundle)

        sendEnhanceEcommerceDataLayerEvent(ADD_TO_CART, bundle)
    }

    fun clickAtcProductBundleSingle(
        atcBundleModel: AddToCartBundleModel,
        bundleName: String,
        bundleId: String,
        bundlePrice: String,
        quantity: String,
        shopName: String,
        shopType: String,
        bundlePriceCut: String,
        shopId: String,
        userId: String,
        selectedPackage: String,
        isFestivity: Boolean
    ) {
        val bundle = Bundle()
        val itemBundle = arrayListOf<Bundle>()
        var productId = ""
        val eventLabel = if(isFestivity){
            joinDash(bundleId, bundleName, bundlePriceCut, selectedPackage, FESTIVITY)
        } else {
            joinDash(bundleId, bundleName, bundlePriceCut, selectedPackage)
        }
        atcBundleModel.addToCartBundleDataModel.data.forEachIndexed { position, productDataModel ->
            itemBundle.add(
                getItemsBundlingAtc(
                    bundleId = bundleId,
                    cartId = productDataModel.cartId,
                    bundleName = bundleName,
                    bundlePrice = bundlePrice,
                    quantity = quantity,
                    shopName = shopName,
                    shopId = productDataModel.shopId,
                    shopType = shopType,
                    bundleType = VALUE_SINGLE_BUNDLING
                )
            )
            productId = productDataModel.productId
        }

        bundle.putString(TrackAppUtils.EVENT, BUNDLING_ADD_TO_CART)
        bundle.putString(TrackAppUtils.EVENT_ACTION, joinDash(CLICK, SINGLE_BUNDLE_WIDGET, BUNDLE_ADD_TO_CART))
        bundle.putString(TrackAppUtils.EVENT_CATEGORY, SHOP_PAGE_BUYER)
        bundle.putString(TrackAppUtils.EVENT_LABEL, eventLabel)
        bundle.putString(TRACKER_ID, TRACKER_ID_ATC_SINGLE_BUNDLING_WIDGET)
        bundle.putString(BUSINESS_UNIT, PHYSICAL_GOODS)
        bundle.putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
        bundle.putString(SHOP_ID, userId)
        bundle.putString(USER_ID, shopId)
        bundle.putString(PRODUCT_ID, productId)
        bundle.putParcelableArrayList(ITEMS, itemBundle)

        sendEnhanceEcommerceDataLayerEvent(ADD_TO_CART, bundle)
    }

    fun clickOnMultipleBundleProduct(
        shopId: String,
        userId: String,
        bundleId: String,
        bundleName: String,
        bundlePriceCut: String,
        bundlePrice: String,
        bundlePosition: Int,
        clickedProduct: ShopHomeBundleProductUiModel,
        isFestivity: Boolean
    ) {
        val bundle = Bundle()
        val eventLabel = if(isFestivity){
            joinDash(bundleId, bundleName, bundlePriceCut, FESTIVITY)
        } else {
            joinDash(bundleId, bundleName, bundlePriceCut)
        }
        val itemList = joinDash(SHOPPAGE, PRODUCT_BUNDLING, MULTIPLE_TYPE)
        val itemBundle = Bundle().apply {
            putString(DIMENSION_117, VALUE_MULTIPLE_BUNDLING)
            putString(DIMENSION_118, bundleId)
            putString(DIMENSION_40, itemList)
            putString(DIMENSION_87, ShopPageTrackingConstant.SHOP_PAGE)
            putString(INDEX, bundlePosition.toString())
            putString(ITEM_BRAND, "")
            putString(ITEM_CATEGORY, "")
            putString(ITEM_ID, bundleId)
            putString(ITEM_NAME, bundleName)
            putString(ITEM_VARIANT, "")
            putString(PRICE, formatPrice(bundlePrice))
        }

        bundle.putString(TrackAppUtils.EVENT, SELECT_CONTENT)
        bundle.putString(TrackAppUtils.EVENT_ACTION, joinDash(CLICK, MULTIPLE_BUNDLE_WIDGET, PRODUCT))
        bundle.putString(TrackAppUtils.EVENT_CATEGORY, SHOP_PAGE_BUYER)
        bundle.putString(TrackAppUtils.EVENT_LABEL, eventLabel)
        bundle.putString(TRACKER_ID, TRACKER_ID_CLICK_MULTIPLE_BUNDLE_PRODUCT)
        bundle.putString(BUSINESS_UNIT, PHYSICAL_GOODS)
        bundle.putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
        bundle.putString(SHOP_ID, shopId)
        bundle.putString(USER_ID, userId)
        bundle.putString(ITEM_LIST, itemList)
        bundle.putParcelableArrayList(ITEMS, arrayListOf(itemBundle))

        sendEnhanceEcommerceDataLayerEvent(SELECT_CONTENT, bundle)
    }

    fun clickOnSingleBundleProduct(
        shopId: String,
        userId: String,
        bundleId: String,
        bundleName: String,
        bundlePriceCut: String,
        bundlePrice: String,
        bundlePosition: Int,
        clickedProduct: ShopHomeBundleProductUiModel,
        selectedPackage: String,
        isFestivity: Boolean
    ) {
        val bundle = Bundle()
        val eventLabel = if(isFestivity){
            joinDash(bundleId, bundleName, bundlePriceCut, selectedPackage, FESTIVITY)
        } else {
            joinDash(bundleId, bundleName, bundlePriceCut, selectedPackage)
        }
        val itemList = joinDash(SHOPPAGE, PRODUCT_BUNDLING, SINGLE_TYPE)
        val itemBundle = Bundle().apply {
            putString(DIMENSION_117, VALUE_SINGLE_BUNDLING)
            putString(DIMENSION_118, bundleId)
            putString(DIMENSION_40, itemList)
            putString(DIMENSION_87, ShopPageTrackingConstant.SHOP_PAGE)
            putString(INDEX, bundlePosition.toString())
            putString(ITEM_BRAND, "")
            putString(ITEM_CATEGORY, "")
            putString(ITEM_ID, bundleId)
            putString(ITEM_NAME, bundleName)
            putString(ITEM_VARIANT, "")
            putString(PRICE, formatPrice(bundlePrice))
        }

        bundle.putString(TrackAppUtils.EVENT, SELECT_CONTENT)
        bundle.putString(TrackAppUtils.EVENT_ACTION, joinDash(CLICK, SINGLE_BUNDLE_WIDGET, PRODUCT))
        bundle.putString(TrackAppUtils.EVENT_CATEGORY, SHOP_PAGE_BUYER)
        bundle.putString(TrackAppUtils.EVENT_LABEL, eventLabel)
        bundle.putString(TRACKER_ID, TRACKER_ID_CLICK_SINGLE_BUNDLE_PRODUCT)
        bundle.putString(BUSINESS_UNIT, PHYSICAL_GOODS)
        bundle.putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
        bundle.putString(PRODUCT_ID, clickedProduct.productId)
        bundle.putString(SHOP_ID, shopId)
        bundle.putString(USER_ID, userId)
        bundle.putString(ITEM_LIST, itemList)
        bundle.putParcelableArrayList(ITEMS, arrayListOf(itemBundle))

        sendEnhanceEcommerceDataLayerEvent(SELECT_CONTENT, bundle)
    }

    fun onTrackSingleVariantChange(
        shopId: String,
        userId: String,
        productId: String,
        bundleName: String,
        bundleId: String,
        bundlePriceCut: String,
        selectedPackage: String,
        isFestivity: Boolean
    ) {
        val eventLabel = if(isFestivity){
            joinDash(bundleId, bundleName, bundlePriceCut, selectedPackage, FESTIVITY)
        } else {
            joinDash(bundleId, bundleName, bundlePriceCut, selectedPackage)
        }
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to CLICK_PG,
            EVENT_ACTION to joinDash(CLICK, SINGLE_BUNDLE_WIDGET, PACKAGE_VARIANT),
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to eventLabel,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            PRODUCT_ID to productId,
            SHOP_ID to shopId,
            USER_ID to userId,
            TRACKER_ID to TRACKER_CLICK_SINGLE_BUNDLING_WIDGET_PACKAGE_VARIANT
        )
        sendDataLayerEvent(eventMap)
    }

    fun impressionMultipleBundleWidget(
        shopId: String,
        userId: String,
        bundleId: String,
        bundleName: String,
        bundlePriceCut: String,
        bundlePrice: String,
        bundlePosition: Int,
        isFestivity: Boolean
    ) {
        val eventLabel = if (isFestivity) {
            joinDash(bundleId, bundleName, bundlePriceCut, FESTIVITY)
        } else {
            joinDash(bundleId, bundleName, bundlePriceCut)
        }
        val itemListValue = joinDash(SHOPPAGE, PRODUCT_BUNDLING, MULTIPLE_TYPE)
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to PROMO_VIEW,
            EVENT_ACTION to joinDash(IMPRESSION, MULTIPLE_BUNDLE_WIDGET),
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to eventLabel,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to shopId,
            USER_ID to userId,
            TRACKER_ID to TRACKER_ID_IMPRESSION_MULTIPLE_BUNDLING_WIDGET,
        )
        eventMap[ECOMMERCE] = mutableMapOf(
            PROMO_VIEW to mutableMapOf(
                PROMOTIONS to listOf(
                    mutableMapOf(
                        DIMENSION_117 to MULTIPLE_BUNDLE_WIDGET,
                        DIMENSION_118 to bundleId,
                        DIMENSION_40 to itemListValue,
                        DIMENSION_87 to ShopPageTrackingConstant.SHOP_PAGE,
                        CREATIVE to "",
                        NAME to bundleName,
                        ID to bundleId,
                        PRICE to formatPrice(bundlePrice),
                        POSITION to bundlePosition
                    )
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    fun impressionSingleBundleWidget(
        shopId: String,
        userId: String,
        productId: String,
        bundleId: String,
        bundleName: String,
        bundlePriceCut: String,
        bundlePrice: String,
        bundlePosition: Int,
        isFestivity: Boolean
    ) {
        val eventLabel = if (isFestivity) {
            joinDash(bundleId, bundleName, bundlePriceCut, FESTIVITY)
        } else {
            joinDash(bundleId, bundleName, bundlePriceCut)
        }
        val eventMap: MutableMap<String, Any> = mutableMapOf(
            EVENT to PROMO_VIEW,
            EVENT_ACTION to joinDash(IMPRESSION, SINGLE_BUNDLE_WIDGET),
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to eventLabel,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            PRODUCT_ID to productId,
            TRACKER_ID to TRACKER_ID_IMPRESSION_SINGLE_BUNDLING_WIDGET,
            SHOP_ID to shopId,
            USER_ID to userId
        )
        val itemListValue = joinDash(SHOPPAGE, PRODUCT_BUNDLING, SINGLE_TYPE)
        eventMap[ECOMMERCE] = mutableMapOf(
            PROMO_VIEW to mutableMapOf(
                PROMOTIONS to listOf(
                    mutableMapOf(
                        DIMENSION_117 to SINGLE_BUNDLE_WIDGET,
                        DIMENSION_118 to bundleId,
                        DIMENSION_40 to itemListValue,
                        DIMENSION_87 to ShopPageTrackingConstant.SHOP_PAGE,
                        CREATIVE to "",
                        NAME to bundleName,
                        ID to bundleId,
                        PRICE to formatPrice(bundlePrice),
                        POSITION to bundlePosition
                    )
                )
            )
        )
        sendDataLayerEvent(eventMap)
    }

    fun impressionThematicWidgetCampaign(
        campaignName: String,
        campaignId: String,
        shopId: String,
        userId: String,
        position: Int,
        isFestivity: Boolean
    ) {
        var eventLabel = joinDash(shopId, campaignId, campaignName)
        if(isFestivity){
            eventLabel = joinDash(eventLabel, FESTIVITY)
        }
        val bundle = getBaseCampaignBundle(
            event = VIEW_ITEM,
            action = THEMATIC_WIDGET_IMPRESSION,
            category = SHOP_PAGE_BUYER,
            label = eventLabel,
            userId = userId,
            shopId = shopId
        ).apply {
            putParcelableArrayList(
                PROMOTIONS,
                arrayListOf(
                    getPromotionCampaignBundle(
                        position = position,
                        campaignId = campaignId,
                        campaignName = campaignName
                    )
                )
            )
            putString(TRACKER_ID, TRACKER_ID_THEMATIC_WIDGET_IMPRESSION)
        }
        sendEnhanceEcommerceDataLayerEvent(VIEW_ITEM, bundle)
    }

    fun impressionProductCardThematicWidgetCampaign(
        campaignId: String,
        campaignName: String,
        shopId: String,
        userId: String,
        products: List<ProductCardUiModel>,
        isFestivity: Boolean
    ) {
        var eventLabel = joinDash(shopId, campaignId, campaignName)
        if(isFestivity){
            eventLabel = joinDash(eventLabel, FESTIVITY)
        }
        val bundle = getBaseCampaignBundle(
            event = VIEW_ITEM_LIST,
            action = THEMATIC_WIDGET_PRODUCT_CARD_IMPRESSION,
            category = SHOP_PAGE_BUYER,
            label = eventLabel,
            userId = userId,
            shopId = shopId
        ).apply {
            val itemListValue = joinDash(VALUE_SHOP_PAGE_THEMATIC, campaignId)
            putString(ITEM_LIST, itemListValue)
            val items = arrayListOf<Bundle>()
            products.forEachIndexed { position, productCardUiModel ->
                items.add(
                    getItemsCampaignBundle(
                        position = position,
                        productId = productCardUiModel.id.orEmpty(),
                        productName = productCardUiModel.name.orEmpty(),
                        productPrice = productCardUiModel.displayedPrice?.getDigits().orZero().toLong(),
                        itemListValue = itemListValue
                    )
                )
            }
            putParcelableArrayList(ITEMS, items)
            putString(TRACKER_ID, TRACKER_ID_THEMATIC_WIDGET_PRODUCT_CARD_IMPRESSION)
            putString(PRODUCT_ID, products.firstOrNull()?.id.orEmpty())
        }
        sendEnhanceEcommerceDataLayerEvent(VIEW_ITEM_LIST, bundle)
    }

    fun clickProductCardThematicWidgetCampaign(
        campaignId: String,
        campaignName: String,
        shopId: String,
        userId: String,
        product: ProductCardUiModel,
        position: Int,
        isFestivity: Boolean
    ) {
        var eventLabel = joinDash(shopId, campaignId, campaignName)
        if(isFestivity){
            eventLabel = joinDash(eventLabel, FESTIVITY)
        }
        val bundle = getBaseCampaignBundle(
            event = SELECT_CONTENT,
            action = THEMATIC_WIDGET_PRODUCT_CARD_CLICK,
            category = SHOP_PAGE_BUYER,
            label = eventLabel,
            userId = userId,
            shopId = shopId
        ).apply {
            val itemListValue = joinDash(VALUE_SHOP_PAGE_THEMATIC, campaignId)
            putString(ITEM_LIST, itemListValue)
            putParcelableArrayList(
                ITEMS,
                arrayListOf(
                    getItemsCampaignBundle(
                        position = position,
                        productId = product.id.orEmpty(),
                        productName = product.name.orEmpty(),
                        productPrice = product.displayedPrice?.getDigits().orZero().toLong(),
                        itemListValue = itemListValue
                    )
                )
            )
            putString(TRACKER_ID, TRACKER_ID_THEMATIC_WIDGET_PRODUCT_CARD_CLICK)
            putString(PRODUCT_ID, product.id.orEmpty())
        }
        sendEnhanceEcommerceDataLayerEvent(SELECT_CONTENT, bundle)
    }

    fun clickSeeAllThematicWidgetCampaign(
        campaignId: String,
        campaignName: String,
        shopId: String,
        userId: String
    ) {
        val bundle = getBaseCampaignBundle(
            event = CLICK_PG,
            action = THEMATIC_WIDGET_SEE_ALL_CLICK,
            category = SHOP_PAGE_BUYER,
            label = joinDash(shopId, campaignId, campaignName),
            userId = userId,
            shopId = shopId
        )
        sendEnhanceEcommerceDataLayerEvent(CLICK_PG, bundle)
    }

    fun clickProductCardSeeAllThematicWidgetCampaign(
        campaignId: String,
        campaignName: String,
        shopId: String,
        userId: String
    ) {
        val bundle = getBaseCampaignBundle(
            event = CLICK_PG,
            action = THEMATIC_WIDGET_PRODUCT_CARD_SEE_ALL_CLICK,
            category = SHOP_PAGE_BUYER,
            label = joinDash(shopId, campaignId, campaignName),
            userId = userId,
            shopId = shopId
        )
        sendEnhanceEcommerceDataLayerEvent(CLICK_PG, bundle)
    }

    private fun getItemsBundlingAtc(
        bundleId: String,
        cartId: String,
        bundleName: String,
        bundlePrice: String,
        quantity: String,
        shopName: String,
        shopId: String,
        shopType: String,
        bundleType: String
    ): Bundle {
        var _valueBundleType = ""
        if (bundleType == VALUE_MULTIPLE_BUNDLING) {
            _valueBundleType = MULTIPLE_TYPE
        } else {
            _valueBundleType = SINGLE_TYPE
        }
        return Bundle().apply {
            putString(CATEGORY_ID, "")
            putString(DIMENSION_117, bundleType)
            putString(DIMENSION_118, bundleId)
            putString(DIMENSION_40, joinDash(SHOPPAGE, PRODUCT_BUNDLING, _valueBundleType))
            putString(DIMENSION_45, cartId)
            putString(DIMENSION_87, SHOP_PAGE)
            putString(ITEM_BRAND, "")
            putString(ITEM_CATEGORY, "")
            putString(ITEM_ID, bundleId)
            putString(ITEM_NAME, bundleName)
            putString(ITEM_VARIANT, "")
            putString(PRICE, formatPrice(bundlePrice))
            putString(QUANTITY, quantity)
            putString(SHOP_ID, shopId)
            putString(SHOP_NAME, shopName)
            putString(SHOP_TYPE, shopType)
        }
    }

    private fun getBaseCampaignBundle(event: String, action: String, category: String, label: String, userId: String, shopId: String): Bundle {
        return Bundle().apply {
            putString(TrackAppUtils.EVENT, event)
            putString(TrackAppUtils.EVENT_ACTION, action)
            putString(TrackAppUtils.EVENT_CATEGORY, category)
            putString(TrackAppUtils.EVENT_LABEL, label)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(SHOP_ID, shopId)
            putString(USER_ID, userId)
        }
    }

    private fun getPromotionCampaignBundle(creativeName: String = "", position: Int, campaignId: String, campaignName: String): Bundle {
        return Bundle().apply {
            putString(CREATIVE_NAME, creativeName)
            putString(CREATIVE_SLOT, (position + 1).toString())
            putString(ITEM_ID, campaignId)
            putString(ITEM_NAME, campaignName)
        }
    }

    private fun getItemsCampaignBundle(
        position: Int,
        productId: String,
        productName: String,
        productPrice: Long,
        productBrand: String = "",
        productCategory: String = "",
        productVariant: String = "",
        itemListValue: String
    ): Bundle {
        return Bundle().apply {
            putString(INDEX, position.toString())
            putString(ITEM_BRAND, productBrand)
            putString(ITEM_CATEGORY, productCategory)
            putString(ITEM_ID, productId)
            putString(ITEM_NAME, productName)
            putString(ITEM_VARIANT, productVariant)
            putString(DIMENSION_40, itemListValue)
            putLong(PRICE, productPrice)
        }
    }

    fun onImpressionProductAtcDirectPurchaseButton(
        shopHomeProductUiModel: ShopHomeProductUiModel,
        widgetName: String,
        position: Int,
        shopId: String,
        userId: String
    ) {
        val eventBundle = Bundle().apply {
            putString(EVENT, VIEW_ITEM)
            putString(EVENT_ACTION, IMPRESSION_PRODUCT_ATC)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER_DIRECT_PURCHASE)
            putString(EVENT_LABEL, "")
            putString(TRACKER_ID, TRACKER_ID_ATC_IMPRESSION)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(PRODUCT_ID, shopHomeProductUiModel.id.orEmpty())
            putString(SHOP_ID, shopId)
            putString(USER_ID, userId)
            putParcelableArrayList(
                PROMOTIONS,
                arrayListOf(
                    createProductAtcDirectPurchaseButtonPromotions(
                        widgetName,
                        position,
                        shopHomeProductUiModel
                    )
                )
            )
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM, eventBundle)
    }

    private fun createProductAtcDirectPurchaseButtonPromotions(
        widgetName: String,
        position: Int,
        shopHomeProductUiModel: ShopHomeProductUiModel
    ): Bundle {
        return Bundle().apply {
            putString(CREATIVE_NAME, widgetName)
            putInt(CREATIVE_SLOT, position)
            putString(ITEM_ID, shopHomeProductUiModel.id.orEmpty())
            putString(ITEM_NAME, shopHomeProductUiModel.name.orEmpty())
        }
    }

    fun onClickProductAtcDirectPurchaseButton(
        atcTrackerModel: ShopPageAtcTracker,
        shopId: String,
        shopType: String,
        shopName: String,
        userId: String
    ) {
        val eventBundle = Bundle().apply {
            putString(EVENT, DIRECT_PURCHASE_ADD_TO_CART)
            putString(EVENT_ACTION, CLICK_PRODUCT_ATC)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER_DIRECT_PURCHASE)
            putString(EVENT_LABEL, atcTrackerModel.componentName)
            putString(TRACKER_ID, TRACKER_ID_ATC_CLICK)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putParcelableArrayList(
                ITEMS,
                arrayListOf(
                    createClickProductAtcDirectPurchaseButtonItems(
                        atcTrackerModel,
                        shopId,
                        shopName,
                        shopType
                    )
                )
            )
            putString(PRODUCT_ID, atcTrackerModel.productId)
            putString(SHOP_ID, shopId)
            putString(USER_ID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(DIRECT_PURCHASE_ADD_TO_CART, eventBundle)
    }

    private fun createClickProductAtcDirectPurchaseButtonItems(
        atcTrackerModel: ShopPageAtcTracker,
        shopId: String,
        shopName: String,
        shopType: String
    ): Bundle {
        return Bundle().apply {
            putString(CATEGORY_ID, NONE)
            putString(DIMENSION_45, atcTrackerModel.cartId)
            putString(ITEM_BRAND, NONE)
            putString(ITEM_CATEGORY, NONE)
            putString(ITEM_ID, atcTrackerModel.productId)
            putString(ITEM_NAME, atcTrackerModel.productName)
            putString(ITEM_VARIANT, atcTrackerModel.isVariant.toString())
            putLong(PRICE, atcTrackerModel.productPrice.digitsOnly().orZero())
            putInt(QUANTITY, atcTrackerModel.quantity)
            putString(ITEMS_SHOP_ID, shopId)
            putString(SHOP_NAME, shopName)
            putString(ITEMS_SHOP_TYPE, shopType)
        }
    }

    fun onClickProductAtcQuantityButton(
        atcTrackerModel: ShopPageAtcTracker,
        shopId: String,
        userId: String
    ) {
        val quantityType = when (atcTrackerModel.atcType) {
            ShopPageAtcTracker.AtcType.UPDATE_ADD -> {
                SHOP_PRODUCT_ATC_QUANTITY_INCREASE
            }
            else -> {
                SHOP_PRODUCT_ATC_QUANTITY_DECREASE
            }
        }
        val eventLabel = "${atcTrackerModel.componentName} - $quantityType"
        val eventMap = mapOf(
            EVENT to CLICK_PG,
            EVENT_ACTION to CLICK_PRODUCT_ATC_QUANTITY,
            EVENT_CATEGORY to SHOP_PAGE_BUYER_DIRECT_PURCHASE,
            EVENT_LABEL to eventLabel,
            TRACKER_ID to TRACKER_ID_ATC_CLICK_QUANTITY,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            PRODUCT_ID to atcTrackerModel.productId,
            SHOP_ID to shopId,
            USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun onClickProductAtcTrashButton(
        atcTrackerModel: ShopPageAtcTracker,
        shopId: String,
        userId: String
    ) {
        val eventMap = mapOf(
            EVENT to CLICK_PG,
            EVENT_ACTION to CLICK_PRODUCT_ATC_RESET,
            EVENT_CATEGORY to SHOP_PAGE_BUYER_DIRECT_PURCHASE,
            EVENT_LABEL to atcTrackerModel.componentName,
            TRACKER_ID to TRACKER_ID_ATC_CLICK_DELETE,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            PRODUCT_ID to atcTrackerModel.productId,
            SHOP_ID to shopId,
            USER_ID to userId,
            DIMENSION_45 to atcTrackerModel.cartId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun impressionPersonalizationTrendingWidget(
        shopId: String,
        userId: String
    ) {
        val eventMap = mapOf(
            EVENT to VIEW_PG_IRIS,
            EVENT_ACTION to IMPRESSION_PERSONALIZATION_TRENDING_WIDGET,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to "",
            TRACKER_ID to TRACKER_ID_IMPRESSION_PERSONALIZATION_TRENDING_WIDGET,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to shopId,
            USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(eventMap)
    }

    fun impressionProductPersonalizationTrendingWidget(
        itemPosition: Int,
        shopHomeProductUiModel: ShopHomeProductUiModel,
        shopId: String,
        userId: String
    ) {
        val eventBundle = Bundle().apply {
            putString(EVENT, VIEW_ITEM_LIST)
            putString(EVENT_ACTION, IMPRESSION_PERSONALIZATION_TRENDING_WIDGET_ITEM)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, "")
            putString(TRACKER_ID, TRACKER_ID_IMPRESSION_PERSONALIZATION_TRENDING_WIDGET_ITEM)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(ITEM_LIST, ITEM_LIST_PERSO_TRENDING_WIDGET)
            putParcelableArrayList(
                ITEMS,
                arrayListOf(
                    createProductPersonalizationTrendingItemMap(
                        shopHomeProductUiModel,
                        itemPosition
                    )
                )
            )
            putString(PRODUCT_ID, shopHomeProductUiModel.id)
            putString(SHOP_ID, shopId)
            putString(USER_ID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM_LIST, eventBundle)
    }

    private fun createProductPersonalizationTrendingItemMap(
        shopHomeProductUiModel: ShopHomeProductUiModel,
        productPosition: Int
    ): Bundle {
        return Bundle().apply {
            putString(DIMENSION_40, ITEM_LIST_PERSO_TRENDING_WIDGET)
            putInt(INDEX, productPosition)
            putString(ITEM_BRAND, "")
            putString(ITEM_CATEGORY, "")
            putString(ITEM_ID, shopHomeProductUiModel.id)
            putString(ITEM_NAME, shopHomeProductUiModel.name)
            putString(ITEM_VARIANT, "")
            putDouble(PRICE, shopHomeProductUiModel.displayedPrice.digitsOnly().toDouble())
        }
    }

    fun clickProductPersonalizationTrendingWidget(
        itemPosition: Int,
        shopHomeProductUiModel: ShopHomeProductUiModel,
        shopId: String,
        userId: String
    ) {
        val eventBundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_ACTION, CLICK_PERSONALIZATION_TRENDING_WIDGET_ITEM)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, shopHomeProductUiModel.id)
            putString(TRACKER_ID, TRACKER_ID_CLICK_PERSONALIZATION_TRENDING_WIDGET_ITEM)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(ITEM_LIST, ITEM_LIST_PERSO_TRENDING_WIDGET)
            putParcelableArrayList(
                ITEMS,
                arrayListOf(
                    createProductPersonalizationTrendingItemMap(
                        shopHomeProductUiModel,
                        itemPosition
                    )
                )
            )
            putString(PRODUCT_ID, shopHomeProductUiModel.id)
            putString(SHOP_ID, shopId)
            putString(USER_ID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventBundle)
    }

    fun impressionPersoProductComparisonWidget(
        trackerModel: ShopHomePersoProductComparisonWidgetImpressionTrackerModel
    ) {
        val eventBundle = Bundle().apply {
            putString(EVENT, VIEW_ITEM_LIST)
            putString(EVENT_ACTION, IMPRESSION_PERSO_PRODUCT_COMPARISON)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, "")
            putString(TRACKER_ID, TRACKER_ID_IMPRESSION_SHOP_PERSO_PRODUCT_COMPARISON)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(ITEM_LIST, ITEM_LIST_PERSO_PRODUCT_COMPARISON)
            putParcelableArrayList(
                ITEMS,
                arrayListOf(createPersoProductComparisonItemMap(trackerModel))
            )
            putString(PRODUCT_ID, trackerModel.productId)
            putString(SHOP_ID, trackerModel.shopId)
            putString(USER_ID, trackerModel.userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(VIEW_ITEM_LIST, eventBundle)
    }

    fun clickPersoProductComparisonWidget(
        trackerModel: ShopHomePersoProductComparisonWidgetImpressionTrackerModel
    ) {
        val eventBundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_ACTION, CLICK_PERSO_PRODUCT_COMPARISON)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, trackerModel.productId)
            putString(TRACKER_ID, TRACKER_ID_CLICK_SHOP_PERSO_PRODUCT_COMPARISON)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(ITEM_LIST, ITEM_LIST_PERSO_PRODUCT_COMPARISON)
            putParcelableArrayList(
                ITEMS,
                arrayListOf(createPersoProductComparisonItemMap(trackerModel))
            )
            putString(PRODUCT_ID, trackerModel.productId)
            putString(SHOP_ID, trackerModel.shopId)
            putString(USER_ID, trackerModel.userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventBundle)
    }

    private fun createPersoProductComparisonItemMap(
        trackerModel: ShopHomePersoProductComparisonWidgetImpressionTrackerModel
    ): Bundle {
        return Bundle().apply {
            putString(DIMENSION_40, ITEM_LIST_PERSO_PRODUCT_COMPARISON)
            putInt(INDEX, trackerModel.position)
            putString(ITEM_BRAND, "")
            putString(ITEM_CATEGORY, "")
            putString(ITEM_ID, trackerModel.productId)
            putString(ITEM_NAME, trackerModel.productName)
            putString(ITEM_VARIANT, "")
            putDouble(PRICE, formatPrice(trackerModel.productPrice).toDoubleOrZero())
        }
    }

    fun impressionDisplayBannerTimerWidget(
        uiModel: ShopWidgetDisplayBannerTimerUiModel,
        position: Int,
        shopId: String,
        userId: String
    ) {
        val widgetTitle = uiModel.header.title
        val statusCampaignTrackerValue = getShopHomeBannerTimerStatusCampaignTrackerValue(
            uiModel.data?.status?.name.orEmpty()
        )
        val eventLabelValue = joinDash(
            uiModel.getCampaignId(),
            statusCampaignTrackerValue,
            position.toString()
        )
        val eventBundle = Bundle().apply {
            putString(EVENT, VIEW_ITEM)
            putString(EVENT_ACTION, IMPRESSION_EXCLUSIVE_LAUNCH_WIDGET)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, eventLabelValue)
            putString(TRACKER_ID, TRACKER_ID_IMPRESSION_BANNER_TIMER_HOME_TAB)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putParcelableArrayList(
                PROMOTIONS,
                arrayListOf(
                    createBannerTimerHomePromotions(
                        uiModel,
                        position,
                        widgetTitle
                    )
                )
            )
            putString(SHOP_ID, shopId)
            putString(USER_ID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventBundle)
    }

    private fun createBannerTimerHomePromotions(
        uiModel: ShopWidgetDisplayBannerTimerUiModel,
        position: Int,
        widgetTitle: String
    ): Bundle {
        val creativeNameValue = getShopHomeBannerTimerStatusCampaignTrackerValue(
            uiModel.data?.status?.name.orEmpty()
        )
        return Bundle().apply {
            putString(CREATIVE_NAME, creativeNameValue)
            putInt(CREATIVE_SLOT, position)
            putString(ITEM_ID, uiModel.getCampaignId())
            putString(ITEM_NAME, widgetTitle)
        }
    }

    private fun getShopHomeBannerTimerStatusCampaignTrackerValue(statusCampaign: String): String {
        return when (statusCampaign.lowercase()) {
            StatusCampaign.UPCOMING.statusCampaign.lowercase() -> {
                VALUE_UPCOMING
            }
            StatusCampaign.ONGOING.statusCampaign.toLowerCase() -> {
                VALUE_ONGOING
            }
            else -> {
                ""
            }
        }
    }

    fun clickRemindMeBannerTimerWidget(
        uiModel: ShopWidgetDisplayBannerTimerUiModel,
        position: Int,
        shopId: String,
        userId: String
    ) {
        val statusCampaignTrackerValue = getShopHomeBannerTimerStatusCampaignTrackerValue(
            uiModel.data?.status?.name.orEmpty()
        )
        val eventLabelValue = joinDash(
            uiModel.getCampaignId(),
            statusCampaignTrackerValue,
            position.toString()
        )
        val event = mapOf(
            EVENT to CLICK_PG,
            EVENT_ACTION to CLICK_REMIND_ME_EXCLUSIVE_LAUNCH_WIDGET,
            EVENT_CATEGORY to SHOP_PAGE_BUYER,
            EVENT_LABEL to eventLabelValue,
            TRACKER_ID to TRACKER_ID_CLICK_REMIND_ME_BANNER_TIMER_HOME_TAB,
            BUSINESS_UNIT to PHYSICAL_GOODS,
            CURRENT_SITE to TOKOPEDIA_MARKETPLACE,
            SHOP_ID to shopId,
            USER_ID to userId
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(event)
    }

    fun clickCtaShopHomeBannerTimerWidget(
        uiModel: ShopWidgetDisplayBannerTimerUiModel,
        position: Int,
        shopId: String,
        userId: String
    ) {
        val widgetTitle = uiModel.header.title
        val statusCampaignTrackerValue = getShopHomeBannerTimerStatusCampaignTrackerValue(
            uiModel.data?.status?.name.orEmpty()
        )
        val eventLabelValue = joinDash(
            uiModel.getCampaignId(),
            statusCampaignTrackerValue,
            position.toString()
        )
        val eventBundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_ACTION, CLICK_EXCLUSIVE_LAUNCH_WIDGET)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, eventLabelValue)
            putString(TRACKER_ID, TRACKER_ID_CLICK_CTA_BANNER_TIMER_HOME_TAB)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putParcelableArrayList(
                PROMOTIONS,
                arrayListOf(
                    createBannerTimerHomePromotions(
                        uiModel,
                        position,
                        widgetTitle
                    )
                )
            )
            putString(SHOP_ID, shopId)
            putString(USER_ID, userId)
        }
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(SELECT_CONTENT, eventBundle)
    }

    //region Showcase navigation widget tracker
    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4148
    // Tracker ID: 45924
    fun sendShowcaseNavigationBannerWidgetImpression(
        shopId: String,
        userId: String,
        uiModel: ShowcaseNavigationUiModel
    ) {
        val eventLabel = when(uiModel.appearance) {
            is LeftMainBannerAppearance -> "hero etalase-left"
            is TopMainBannerAppearance -> "hero etalase-top"
            is CarouselAppearance -> "no hero etalase"
        }

        val showcases = when (uiModel.appearance) {
            is CarouselAppearance -> uiModel.appearance.showcases
            is LeftMainBannerAppearance -> {
                val showcases = mutableListOf<Showcase>()
                uiModel.appearance.tabs.forEach { tabs ->
                    tabs.showcases.forEach { showcase ->
                        showcases.add(showcase)
                    }
                }
                showcases
            }
            is TopMainBannerAppearance -> uiModel.appearance.showcases
        }


        val bundledShowcases = showcases
            .take(5)
            .mapIndexed { index, showcase ->
                Bundle().apply {
                    putString(CREATIVE_NAME, "")
                    putInt(CREATIVE_SLOT, index)
                    putString(ITEM_ID, showcase.id)
                    putString(ITEM_NAME, showcase.name)
                }
            }

        val promotions = ArrayList(bundledShowcases)

        val bundle = Bundle().apply {
            putString(EVENT, VIEW_ITEM)
            putString(EVENT_ACTION, IMPRESSION_REIMAGINED_SHOWCASE_NAVIGATION)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, eventLabel)
            putString(TRACKER_ID, TRACKER_ID_IMPRESSION_REIMAGINED_SHOWCASE_NAVIGATION)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS_PASCAL_CASE)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(SHOP_ID, shopId)
            putString(USER_ID, userId)
            putParcelableArrayList(PROMOTIONS, promotions)
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(PROMO_VIEW, bundle)
    }


    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4148
    // Tracker ID: 45925
    fun sendShowcaseNavigationBannerWidgetClick(shopId: String, userId: String, uiModel: ShowcaseNavigationUiModel, showcase: Showcase) {
        val eventLabel = when(uiModel.appearance) {
            is LeftMainBannerAppearance -> "hero etalase-left - ${showcase.id}"
            is TopMainBannerAppearance -> "hero etalase-top - ${showcase.id}"
            is CarouselAppearance -> "no hero etalase - ${showcase.id}"
        }

        val bundledShowcase = Bundle().apply {
            putString(CREATIVE_NAME, "")
            putInt(CREATIVE_SLOT, 0)
            putString(ITEM_ID, showcase.id)
            putString(ITEM_NAME, showcase.name)
        }

        val bundle = Bundle().apply {
            putString(EVENT, SELECT_CONTENT)
            putString(EVENT_ACTION, CLICK_REIMAGINED_SHOWCASE_NAVIGATION)
            putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
            putString(EVENT_LABEL, eventLabel)
            putString(TRACKER_ID, TRACKER_ID_CLICK_REIMAGINED_SHOWCASE_NAVIGATION)
            putString(BUSINESS_UNIT, PHYSICAL_GOODS_PASCAL_CASE)
            putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
            putString(SHOP_ID, shopId)
            putString(USER_ID, userId)
            putParcelableArrayList(PROMOTIONS, arrayListOf(bundledShowcase))
        }

        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(PROMO_CLICK, bundle)
    }


    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4148
    // Tracker ID: 45926
    fun sendShowcaseNavigationBannerWidgetViewAllClick(
        uiModel: ShowcaseNavigationUiModel,
        showcase: Showcase,
        shopId: String,
        userId: String
    ) {
        val eventLabel = when(uiModel.appearance) {
            is LeftMainBannerAppearance -> "hero etalase-left - ${showcase.id}"
            is TopMainBannerAppearance -> "hero etalase-top - ${showcase.id}"
            is CarouselAppearance -> "no hero etalase - ${showcase.id}"
        }

        Tracker.Builder()
            .setEvent(CLICK_PG)
            .setEventAction(CLICK_REIMAGINED_SHOWCASE_NAVIGATION_VIEW_ALL)
            .setEventCategory(SHOP_PAGE_BUYER)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_CLICK_VIEW_ALL_REIMAGINED_SHOWCASE_NAVIGATION)
            .setBusinessUnit(PHYSICAL_GOODS_PASCAL_CASE)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(shopId)
            .setUserId(userId)
            .build()
            .send()
    }


    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4148
    // Tracker ID: 45927
    fun sendShowcaseNavigationBannerTabImpression(
        tabName: String,
        showcaseId: String,
        shopId: String,
        userId: String
    ) {
        val eventLabel = "$tabName - $showcaseId"

        Tracker.Builder()
            .setEvent(VIEW_PG_IRIS)
            .setEventAction(IMPRESSION_REIMAGINED_SHOWCASE_NAVIGATION_WITH_TAB)
            .setEventCategory(SHOP_PAGE_BUYER)
            .setEventLabel(eventLabel)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_IMPRESSION_REIMAGINED_SHOWCASE_NAVIGATION_WITH_TAB)
            .setBusinessUnit(PHYSICAL_GOODS_PASCAL_CASE)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(shopId)
            .setUserId(userId)
            .build()
            .send()
    }


    // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4148
    // Tracker ID: 45948
    fun sendShowcaseNavigationTabClick(
        tabName: String,
        shopId: String,
        userId: String
    ) {
        Tracker.Builder()
            .setEvent(CLICK_PG)
            .setEventAction(CLICK_REIMAGINED_SHOWCASE_NAVIGATION_WITH_TAB)
            .setEventCategory(SHOP_PAGE_BUYER)
            .setEventLabel(tabName)
            .setCustomProperty(TRACKER_ID, TRACKER_ID_CLICK_REIMAGINED_SHOWCASE_NAVIGATION_WITH_TAB)
            .setBusinessUnit(PHYSICAL_GOODS_PASCAL_CASE)
            .setCurrentSite(TOKOPEDIA_MARKETPLACE)
            .setShopId(shopId)
            .setUserId(userId)
            .build()
            .send()
    }



     // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4148
     // Tracker ID: 45949
     fun sendShowcaseNavigationMainBannerClick(
         tabName: String,
         showcase: Showcase,
         shopId: String,
         userId: String
     ) {
         val eventLabel = "$tabName - ${showcase.id}"
         val bundledShowcase = Bundle().apply {
             putString(CREATIVE_NAME, "")
             putInt(CREATIVE_SLOT, 0)
             putString(ITEM_ID, showcase.id)
             putString(ITEM_NAME, showcase.name)
         }

         val bundle = Bundle().apply {
             putString(EVENT, SELECT_CONTENT)
             putString(EVENT_ACTION, CLICK_REIMAGINED_BANNER_SHOWCASE_NAVIGATION_WITH_TAB)
             putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
             putString(EVENT_LABEL, eventLabel)
             putString(TRACKER_ID, TRACKER_ID_CLICK_REIMAGINED_BANNER_SHOWCASE_NAVIGATION_WITH_TAB)
             putString(BUSINESS_UNIT, PHYSICAL_GOODS_PASCAL_CASE)
             putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
             putString(SHOP_ID, shopId)
             putString(USER_ID, userId)
             putParcelableArrayList(PROMOTIONS, arrayListOf(bundledShowcase))
         }

         TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(PROMO_CLICK, bundle)
     }
     //endregion


     //region Product carousel widget tracker
     // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4148
     // Tracker ID: 45950
     fun sendProductCarouselImpression(
         widgetId: String,
         widgetStyle: String,
         products: List<ProductItemType>,
         shopId: String,
         userId: String
     ) {

         val showProductInfo = products.firstOrNull()?.showProductInfo.orFalse()
         val productCardVariant = if (showProductInfo) "with_info" else "without_info"

         val bundledProducts = products.mapIndexed { index, product ->
             Bundle().apply {
                 putString(CREATIVE_NAME, "")
                 putInt(CREATIVE_SLOT, index)
                 putString(ITEM_ID, widgetId)
                 putString(ITEM_NAME, product.name)
             }
         }


         val bannerVariant = when (widgetStyle) {
             WidgetStyle.VERTICAL.id -> "vertical"
             WidgetStyle.HORIZONTAL.id -> "horizontal"
             else -> "no_banner"
         }

         val eventLabel = "$bannerVariant - $productCardVariant"
         val promotions = ArrayList(bundledProducts)

         val bundle = Bundle().apply {
             putString(EVENT, VIEW_ITEM)
             putString(EVENT_ACTION, IMPRESSION_REIMAGINED_BANNER_PRODUCT_CAROUSEL)
             putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
             putString(EVENT_LABEL, eventLabel)
             putString(TRACKER_ID, TRACKER_ID_IMPRESSION_REIMAGINED_BANNER_PRODUCT_CAROUSEL)
             putString(BUSINESS_UNIT, PHYSICAL_GOODS_PASCAL_CASE)
             putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
             putString(SHOP_ID, shopId)
             putString(USER_ID, userId)
             putParcelableArrayList(PROMOTIONS, promotions)
         }

         TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(PROMO_VIEW, bundle)
     }


     // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4148
     // Tracker ID: 45951
     fun sendProductCarouselMainBannerClick(
         widgetId: String,
         widgetName: String,
         widgetStyle: String,
         shopId: String,
         userId: String
     ) {
         val bundledShowcase = Bundle().apply {
             putString(CREATIVE_NAME, "")
             putInt(CREATIVE_SLOT, 0)
             putString(ITEM_ID, widgetId)
             putString(ITEM_NAME, widgetName)
         }

         val bundle = Bundle().apply {
             putString(EVENT, SELECT_CONTENT)
             putString(EVENT_ACTION, CLICK_REIMAGINED_BANNER_PRODUCT_CAROUSEL)
             putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
             putString(EVENT_LABEL, widgetStyle)
             putString(TRACKER_ID, TRACKER_ID_CLICK_REIMAGINED_BANNER_PRODUCT_CAROUSEL)
             putString(BUSINESS_UNIT, PHYSICAL_GOODS_PASCAL_CASE)
             putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
             putString(SHOP_ID, shopId)
             putString(USER_ID, userId)
             putParcelableArrayList(PROMOTIONS, arrayListOf(bundledShowcase))
         }

         TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(PROMO_CLICK, bundle)
     }


     // Tracker URL: https://mynakama.tokopedia.com/datatracker/requestdetail/view/4148
     // Tracker ID: 45952
     fun sendProductCarouselProductClick(
         product: ProductItemType,
         widgetStyle: String,
         shopId: String,
         userId: String
     ) {
         val bannerVariant = when (widgetStyle) {
             WidgetStyle.VERTICAL.id -> "vertical"
             WidgetStyle.HORIZONTAL.id -> "horizontal"
             else -> "no_banner"
         }

         val productCardVariant = if (product.showProductInfo) {
             "with_info"
         } else {
             "without_info"
         }

         val eventLabel = "$bannerVariant - $productCardVariant"

         val bundledShowcase = Bundle().apply {
             putString(CREATIVE_NAME, "")
             putInt(CREATIVE_SLOT, 0)
             putString(ITEM_ID, product.productId)
             putString(ITEM_NAME, product.name)
         }

         val bundle = Bundle().apply {
             putString(EVENT, SELECT_CONTENT)
             putString(EVENT_ACTION, CLICK_REIMAGINED_PRODUCT_CAROUSEL)
             putString(EVENT_CATEGORY, SHOP_PAGE_BUYER)
             putString(EVENT_LABEL, eventLabel)
             putString(TRACKER_ID, TRACKER_ID_CLICK_REIMAGINED_PRODUCT_CAROUSEL)
             putString(BUSINESS_UNIT, PHYSICAL_GOODS_PASCAL_CASE)
             putString(CURRENT_SITE, TOKOPEDIA_MARKETPLACE)
             putString(SHOP_ID, shopId)
             putString(USER_ID, userId)
             putParcelableArrayList(PROMOTIONS, arrayListOf(bundledShowcase))
         }

         TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(PRODUCT_CLICK, bundle)
     }
     //endregion
}
