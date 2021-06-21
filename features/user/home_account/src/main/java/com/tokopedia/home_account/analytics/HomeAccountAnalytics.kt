package com.tokopedia.home_account.analytics

import android.content.Context
import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.home_account.AccountConstants.Analytics.ACCOUNT
import com.tokopedia.home_account.AccountConstants.Analytics.ACTION_FIELD
import com.tokopedia.home_account.AccountConstants.Analytics.Action.ACTION_CLICK_ABOUT_TOKOPEDIA_SECTION
import com.tokopedia.home_account.AccountConstants.Analytics.Action.ACTION_CLICK_ACCOUNT_SETTING_SECTION
import com.tokopedia.home_account.AccountConstants.Analytics.Action.ACTION_CLICK_APP_SETTING_SECTION
import com.tokopedia.home_account.AccountConstants.Analytics.Action.ACTION_CLICK_BALANCE
import com.tokopedia.home_account.AccountConstants.Analytics.Action.ACTION_CLICK_LOGOUT
import com.tokopedia.home_account.AccountConstants.Analytics.Action.ACTION_CLICK_ON_MORE_OPTION
import com.tokopedia.home_account.AccountConstants.Analytics.Action.ACTION_CLICK_PRODUCT_RECOMMENDATION
import com.tokopedia.home_account.AccountConstants.Analytics.Action.ACTION_CLICK_PROFILE
import com.tokopedia.home_account.AccountConstants.Analytics.Action.ACTION_CLICK_REWARD_SECTION
import com.tokopedia.home_account.AccountConstants.Analytics.Action.ACTION_CLICK_TOGGLE_ON_GEOLOCATION
import com.tokopedia.home_account.AccountConstants.Analytics.Action.ACTION_CLICK_TOKOPOINTS
import com.tokopedia.home_account.AccountConstants.Analytics.Action.ACTION_IMPRESSION_PRODUCT_RECOMMENDATION
import com.tokopedia.home_account.AccountConstants.Analytics.Action.ACTION_SIMPAN_THEME_SELECTION
import com.tokopedia.home_account.AccountConstants.Analytics.Action.ACTION_VIEW_OVO_HOMEPAGE
import com.tokopedia.home_account.AccountConstants.Analytics.BusinessUnit.USER_PLATFORM_UNIT
import com.tokopedia.home_account.AccountConstants.Analytics.CLICK
import com.tokopedia.home_account.AccountConstants.Analytics.CURRENCY_CODE
import com.tokopedia.home_account.AccountConstants.Analytics.Category.CATEGORY_ACCOUNT_BUYER
import com.tokopedia.home_account.AccountConstants.Analytics.Category.CATEGORY_ACCOUNT_PAGE_BUYER
import com.tokopedia.home_account.AccountConstants.Analytics.Category.CATEGORY_HOMEPAGE
import com.tokopedia.home_account.AccountConstants.Analytics.Category.CATEGORY_OVO_HOMEPAGE
import com.tokopedia.home_account.AccountConstants.Analytics.Category.CATEGORY_SETTING_PAGE
import com.tokopedia.home_account.AccountConstants.Analytics.CurrentSite.TOKOPEDIA_MARKETPLACE_SITE
import com.tokopedia.home_account.AccountConstants.Analytics.DATA_ATTRIBUTION
import com.tokopedia.home_account.AccountConstants.Analytics.DATA_BRAND
import com.tokopedia.home_account.AccountConstants.Analytics.DATA_CATEGORY
import com.tokopedia.home_account.AccountConstants.Analytics.DATA_DIMENSION_83
import com.tokopedia.home_account.AccountConstants.Analytics.DATA_ID
import com.tokopedia.home_account.AccountConstants.Analytics.DATA_NAME
import com.tokopedia.home_account.AccountConstants.Analytics.DATA_POSITION
import com.tokopedia.home_account.AccountConstants.Analytics.DATA_PRICE
import com.tokopedia.home_account.AccountConstants.Analytics.DATA_VARIAN
import com.tokopedia.home_account.AccountConstants.Analytics.ECOMMERCE
import com.tokopedia.home_account.AccountConstants.Analytics.EVENT
import com.tokopedia.home_account.AccountConstants.Analytics.EVENT_ACTION
import com.tokopedia.home_account.AccountConstants.Analytics.EVENT_BUSINESS_UNIT
import com.tokopedia.home_account.AccountConstants.Analytics.EVENT_CATEGORY
import com.tokopedia.home_account.AccountConstants.Analytics.EVENT_CURRENT_SITE
import com.tokopedia.home_account.AccountConstants.Analytics.EVENT_LABEL
import com.tokopedia.home_account.AccountConstants.Analytics.EVENT_USER_ID
import com.tokopedia.home_account.AccountConstants.Analytics.Event.EVENT_CLICK_ACCOUNT
import com.tokopedia.home_account.AccountConstants.Analytics.Event.EVENT_CLICK_HOME_PAGE
import com.tokopedia.home_account.AccountConstants.Analytics.Event.EVENT_CLICK_SETTING
import com.tokopedia.home_account.AccountConstants.Analytics.Event.EVENT_PRODUCT_CLICK
import com.tokopedia.home_account.AccountConstants.Analytics.Event.EVENT_PRODUCT_VIEW
import com.tokopedia.home_account.AccountConstants.Analytics.IDR
import com.tokopedia.home_account.AccountConstants.Analytics.IMPRESSIONS
import com.tokopedia.home_account.AccountConstants.Analytics.LIST
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_ABOUT_TOKOPEDIA
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_ACCOUNT_SECURITY
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_APP_SETTING
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_BANK_ACCOUNT
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_CLEAN_CACHE
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_DISABLE
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_EMPTY
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_ENABLE
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_GEOLOCATION
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_GET_TO_KNOW_TOKOPEDIA
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_IMAGE_QUALITY
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_INSTANT_BUY
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_INSTANT_PAYMENT
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_IP
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_LIST_ADDRESS
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_MEMBER
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_MEMBER_STORE
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_MY_COUPON
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_NOTIFICATION
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_PRIVACY_POLICY
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_REVIEW_THIS_APP
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_SAFE_MODE
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_SHAKE
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_STICKER_TOKOPEDIA
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_TERMS_AND_CONDITIONS
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_TOP_QUEST
import com.tokopedia.home_account.AccountConstants.Analytics.Label.LABEL_VIEW_MODE
import com.tokopedia.home_account.AccountConstants.Analytics.NONE_OTHER
import com.tokopedia.home_account.AccountConstants.Analytics.PRODUCTS
import com.tokopedia.home_account.AccountConstants.Analytics.SETTING
import com.tokopedia.home_account.AccountConstants.Analytics.SHOP
import com.tokopedia.home_account.AccountConstants.Analytics.USER
import com.tokopedia.home_account.AccountConstants.Analytics.VALUE_BEBAS_ONGKIR
import com.tokopedia.home_account.AccountConstants.Analytics.VALUE_PRODUCT_RECOMMENDATION_LIST
import com.tokopedia.home_account.AccountConstants.Analytics.VALUE_PRODUCT_TOPADS
import com.tokopedia.home_account.AccountConstants.Analytics.VALUE_WISHLIST_PRODUCT
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import com.tokopedia.track.interfaces.Analytics
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.user.session.UserSessionInterface
import java.util.*

/**
 * Created by Yoris Prayogo on 27/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountAnalytics(val context: Context, val userSession: UserSessionInterface) {

    fun eventClickSetting(item: String?) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_HOME_PAGE,
                String.format("%s %s", USER, SETTING),
                String.format("%s %s", CLICK, item),
                LABEL_EMPTY
        ))
    }

    fun eventClickAccountSetting(item: String?) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_HOME_PAGE,
                String.format("%s %s", ACCOUNT, SETTING),
                String.format("%s %s", CLICK, item),
                LABEL_EMPTY
        ))
    }

    fun eventClickPaymentSetting(item: String?) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_HOME_PAGE,
                String.format("%s %s", SHOP, SETTING),
                String.format("%s %s", CLICK, item),
                LABEL_EMPTY
        ))
    }

    fun eventClickToggleOnGeolocation() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(
                EVENT_CLICK_HOME_PAGE,
                CATEGORY_HOMEPAGE,
                ACTION_CLICK_TOGGLE_ON_GEOLOCATION,
                LABEL_EMPTY
        )
    }

    fun eventClickToggleOffGeolocation() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(
                EVENT_CLICK_HOME_PAGE,
                CATEGORY_HOMEPAGE,
                ACTION_CLICK_TOGGLE_ON_GEOLOCATION,
                LABEL_EMPTY
        )
    }

    fun eventAccountProductView(trackingQueue: TrackingQueue, recommendationItem: RecommendationItem, position: Int) {
        val map: Map<String, Any> = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_VIEW,
                EVENT_CATEGORY, CATEGORY_ACCOUNT_PAGE_BUYER,
                EVENT_ACTION, ACTION_IMPRESSION_PRODUCT_RECOMMENDATION,
                EVENT_LABEL, "",
                ECOMMERCE, DataLayer.mapOf(CURRENCY_CODE, IDR,
                IMPRESSIONS, DataLayer.listOf(
                addAccountProductViewImpressions(recommendationItem, position)
        )
        ))
        trackingQueue.putEETracking(map as HashMap<String, Any>)
    }

    private fun addAccountProductViewImpressions(recommendationItem: RecommendationItem, position: Int): Any? {
        var list: String? = kotlin.String.format(VALUE_PRODUCT_RECOMMENDATION_LIST, recommendationItem.recommendationType)
        if (recommendationItem.isTopAds) {
            list += VALUE_PRODUCT_TOPADS
        }
        return DataLayer.mapOf(DATA_NAME, recommendationItem.name,
                DATA_ID, recommendationItem.productId,
                DATA_PRICE, recommendationItem.price.replace("[^0-9]".toRegex(), ""),
                DATA_BRAND, NONE_OTHER,
                DATA_CATEGORY, recommendationItem.categoryBreadcrumbs,
                DATA_VARIAN, NONE_OTHER,
                LIST, list,
                DATA_POSITION, position.toString(),
                DATA_DIMENSION_83, if (recommendationItem.isFreeOngkirActive) VALUE_BEBAS_ONGKIR else NONE_OTHER)
    }

    fun eventAccountProductClick(recommendationItem: RecommendationItem, position: Int, widgetTitle: String?) {
        var list = String.format(VALUE_PRODUCT_RECOMMENDATION_LIST, recommendationItem.recommendationType)
        if (recommendationItem.isTopAds) {
            list += VALUE_PRODUCT_TOPADS
        }
        val tracker: Analytics = TrackApp.getInstance().gtm
        val map = DataLayer.mapOf(
                EVENT, EVENT_PRODUCT_CLICK,
                EVENT_CATEGORY, CATEGORY_ACCOUNT_PAGE_BUYER,
                EVENT_ACTION, ACTION_CLICK_PRODUCT_RECOMMENDATION,
                EVENT_LABEL, LABEL_EMPTY,
                ECOMMERCE, DataLayer.mapOf(
                CLICK, DataLayer.mapOf(
                ACTION_FIELD,
                DataLayer.mapOf(LIST, list),
                PRODUCTS,
                DataLayer.listOf(
                        DataLayer.mapOf(
                                DATA_NAME, recommendationItem.name,
                                DATA_ID, recommendationItem.productId,
                                DATA_PRICE, recommendationItem.price.replace("[^0-9]".toRegex(), ""),
                                DATA_BRAND, NONE_OTHER,
                                DATA_CATEGORY, recommendationItem.categoryBreadcrumbs,
                                DATA_VARIAN, NONE_OTHER,
                                LIST, widgetTitle,
                                DATA_POSITION, position.toString(),
                                DATA_ATTRIBUTION, NONE_OTHER,
                                DATA_DIMENSION_83, if (recommendationItem.isFreeOngkirActive) VALUE_BEBAS_ONGKIR else NONE_OTHER
                        )
                )
        )
        )
        )
        tracker.sendEnhanceEcommerceEvent(map)
    }

    fun eventClickWishlistButton(wishlistStatus: Boolean) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        var status: String = if (wishlistStatus) {
            "add"
        } else {
            "remove"
        }
        analytics.sendGeneralEvent(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_PAGE_BUYER,
                String.format(VALUE_WISHLIST_PRODUCT, status),
                ""
        )
    }

    fun eventClickProfile() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_PROFILE,
                LABEL_EMPTY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)

    }

    fun eventViewOvoHomepage() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_OVO_HOMEPAGE,
                ACTION_VIEW_OVO_HOMEPAGE,
                LABEL_EMPTY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickBalance() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_BALANCE,
                LABEL_EMPTY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickTokopoints() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_TOKOPOINTS,
                LABEL_EMPTY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickOnMoreMemberOption() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_ON_MORE_OPTION,
                LABEL_MEMBER
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickRewardMemberStore() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_REWARD_SECTION,
                LABEL_MEMBER_STORE
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickRewardTopQuest() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_REWARD_SECTION,
                LABEL_TOP_QUEST
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickRewardMyCoupon() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_REWARD_SECTION,
                LABEL_MY_COUPON
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickAccountSettingListAddress() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_ACCOUNT_SETTING_SECTION,
                LABEL_LIST_ADDRESS
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickAccountSettingBankAccount() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_ACCOUNT_SETTING_SECTION,
                LABEL_BANK_ACCOUNT
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickAccountSettingInstantPayment() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_ACCOUNT_SETTING_SECTION,
                LABEL_INSTANT_PAYMENT
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickAccountSettingInstantBuy() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_ACCOUNT_SETTING_SECTION,
                LABEL_INSTANT_BUY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickAccountSettingAccountSecurity() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_ACCOUNT_SETTING_SECTION,
                LABEL_ACCOUNT_SECURITY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickAccountSettingNotification() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_ACCOUNT_SETTING_SECTION,
                LABEL_NOTIFICATION
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickOnMoreAppSettingOption() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_ON_MORE_OPTION,
                LABEL_APP_SETTING
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickAppSettingShake(enable: Boolean) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_APP_SETTING_SECTION,
                String.format("%s - %s", LABEL_SHAKE, if (enable) LABEL_ENABLE else LABEL_DISABLE)
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickAppSettingGeolocation(enable: Boolean) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_APP_SETTING_SECTION,
                String.format("%s - %s", LABEL_GEOLOCATION, if (enable) LABEL_ENABLE else LABEL_DISABLE)
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickAppSettingSafeMode(enable: Boolean) {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_APP_SETTING_SECTION,
                String.format("%s - %s", LABEL_SAFE_MODE, if (enable) LABEL_ENABLE else LABEL_DISABLE)
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }


    fun eventClickAppSettingStickerTokopedia() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_APP_SETTING_SECTION,
                LABEL_STICKER_TOKOPEDIA
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickAppSettingImageQuality() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_APP_SETTING_SECTION,
                LABEL_IMAGE_QUALITY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickAppSettingCleanCache() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_APP_SETTING_SECTION,
                LABEL_CLEAN_CACHE
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickAppSettingViewMode() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_APP_SETTING_SECTION,
                LABEL_VIEW_MODE
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickOnMoreAboutTokopediaOption() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_ON_MORE_OPTION,
                LABEL_ABOUT_TOKOPEDIA
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickGetToKnowAboutTokopedia() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_ABOUT_TOKOPEDIA_SECTION,
                LABEL_GET_TO_KNOW_TOKOPEDIA
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickTermsAndConditionsAboutTokopedia() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_ABOUT_TOKOPEDIA_SECTION,
                LABEL_TERMS_AND_CONDITIONS
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickPrivacyPolicyAboutTokopedia() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_ABOUT_TOKOPEDIA_SECTION,
                LABEL_PRIVACY_POLICY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickIpAboutTokopedia() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_ABOUT_TOKOPEDIA_SECTION,
                LABEL_IP
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickReviewAboutTokopedia() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_ABOUT_TOKOPEDIA_SECTION,
                LABEL_REVIEW_THIS_APP
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickLogout() {
        val analytics: Analytics = TrackApp.getInstance().gtm
        val map = TrackAppUtils.gtmData(
                EVENT_CLICK_ACCOUNT,
                CATEGORY_ACCOUNT_BUYER,
                ACTION_CLICK_LOGOUT,
                LABEL_EMPTY
        )
        map[EVENT_BUSINESS_UNIT] = USER_PLATFORM_UNIT
        map[EVENT_CURRENT_SITE] = TOKOPEDIA_MARKETPLACE_SITE
        map[EVENT_USER_ID] = userSession.userId
        analytics.sendGeneralEvent(map)
    }

    fun eventClickThemeSetting(isDarkMode: Boolean) {
        val label: String = if (isDarkMode) "dark" else "light"
        val analytics: Analytics = TrackApp.getInstance().gtm
        analytics.sendGeneralEvent(TrackAppUtils.gtmData(
                EVENT_CLICK_SETTING,
                CATEGORY_SETTING_PAGE,
                ACTION_SIMPAN_THEME_SELECTION,
                label
        ))
    }
}
