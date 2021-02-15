package com.tokopedia.seller.search.feature.analytics

import com.tokopedia.analyticconstant.DataLayer
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.BUSINESS_UNIT
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.CLICK_BACK_BUTTON
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.CLICK_DELETE_ALL_SEARCH
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.CLICK_DELETE_SELECTED_SEARCH
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.CLICK_ON_SEARCH_RESULT
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.CLICK_OTHER_RESULT
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.CLICK_RECOMMEND_WORDING
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.CLICK_SEARCH
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.CLICK_X_SEARCH_BOX
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.CURRENT_SITE
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.EVENT
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.EVENT_ACTION
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.EVENT_CATEGORY
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.EVENT_LABEL
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.GLOBAL_SEARCH
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.GLOBAL_SEARCH_SCREEN
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.IMPRESSION_EMPTY_RESULT
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.PHYSICAL_GOODS
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.SCREEN_NAME
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.SUGGESTED_SEARCH
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.TOKOPEDIA_SELLER
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.USER_ID
import com.tokopedia.seller.search.feature.analytics.SellerSearchTrackingConstant.VIEW_SEARCH_IRIS
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics

object SellerSearchTracking {

    private val tracker: ContextAnalytics by lazy { TrackApp.getInstance().gtm }

    fun sendScreenSearchEvent(userId: String) {
        val dataLayer = mutableMapOf<String, String>()
        dataLayer[CURRENT_SITE] = TOKOPEDIA_SELLER
        dataLayer[USER_ID] = userId
        dataLayer[BUSINESS_UNIT] = PHYSICAL_GOODS
        tracker.sendScreenAuthenticated(GLOBAL_SEARCH_SCREEN, dataLayer)
    }

    fun clickBackButtonSearchEvent(userId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_SEARCH,
                EVENT_CATEGORY, GLOBAL_SEARCH,
                EVENT_ACTION, CLICK_BACK_BUTTON,
                EVENT_LABEL, "",
                SCREEN_NAME, GLOBAL_SEARCH_SCREEN,
                CURRENT_SITE, TOKOPEDIA_SELLER,
                USER_ID, userId,
                BUSINESS_UNIT, PHYSICAL_GOODS
        ))
    }

    fun impressionEmptyResultEvent(userId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, VIEW_SEARCH_IRIS,
                EVENT_CATEGORY, GLOBAL_SEARCH,
                EVENT_ACTION, IMPRESSION_EMPTY_RESULT,
                EVENT_LABEL, "",
                SCREEN_NAME, GLOBAL_SEARCH_SCREEN,
                CURRENT_SITE, TOKOPEDIA_SELLER,
                USER_ID, userId,
                BUSINESS_UNIT, PHYSICAL_GOODS
        ))
    }

    fun clickDeleteAllSearchEvent(userId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_SEARCH,
                EVENT_CATEGORY, GLOBAL_SEARCH,
                EVENT_ACTION, CLICK_DELETE_ALL_SEARCH,
                EVENT_LABEL, "",
                SCREEN_NAME, GLOBAL_SEARCH_SCREEN,
                CURRENT_SITE, TOKOPEDIA_SELLER,
                USER_ID, userId,
                BUSINESS_UNIT, PHYSICAL_GOODS
        ))
    }

    fun clickDeleteSelectedSearch(userId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_SEARCH,
                EVENT_CATEGORY, GLOBAL_SEARCH,
                EVENT_ACTION, CLICK_DELETE_SELECTED_SEARCH,
                EVENT_LABEL, "",
                SCREEN_NAME, GLOBAL_SEARCH_SCREEN,
                CURRENT_SITE, TOKOPEDIA_SELLER,
                USER_ID, userId,
                BUSINESS_UNIT, PHYSICAL_GOODS
        ))
    }

    fun clickRecommendWordingEvent(userId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_SEARCH,
                EVENT_CATEGORY, GLOBAL_SEARCH,
                EVENT_ACTION, CLICK_RECOMMEND_WORDING,
                EVENT_LABEL, "",
                SCREEN_NAME, GLOBAL_SEARCH_SCREEN,
                CURRENT_SITE, TOKOPEDIA_SELLER,
                USER_ID, userId,
                BUSINESS_UNIT, PHYSICAL_GOODS
        ))
    }

    fun clickClearSearchBoxEvent(userId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_SEARCH,
                EVENT_CATEGORY, GLOBAL_SEARCH,
                EVENT_ACTION, CLICK_X_SEARCH_BOX,
                EVENT_LABEL, "",
                SCREEN_NAME, GLOBAL_SEARCH_SCREEN,
                CURRENT_SITE, TOKOPEDIA_SELLER,
                USER_ID, userId,
                BUSINESS_UNIT, PHYSICAL_GOODS
        ))
    }

    fun clickOtherResult(userId: String, section: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_SEARCH,
                EVENT_CATEGORY, GLOBAL_SEARCH,
                EVENT_ACTION, CLICK_OTHER_RESULT,
                EVENT_LABEL, section,
                SCREEN_NAME, GLOBAL_SEARCH_SCREEN,
                CURRENT_SITE, TOKOPEDIA_SELLER,
                USER_ID, userId,
                BUSINESS_UNIT, PHYSICAL_GOODS
        ))
    }

    fun clickOnSearchResult(userId: String, section: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_SEARCH,
                EVENT_CATEGORY, GLOBAL_SEARCH,
                EVENT_ACTION, CLICK_ON_SEARCH_RESULT,
                EVENT_LABEL, section,
                SCREEN_NAME, GLOBAL_SEARCH_SCREEN,
                CURRENT_SITE, TOKOPEDIA_SELLER,
                USER_ID, userId,
                BUSINESS_UNIT, PHYSICAL_GOODS
        ))
    }

    fun clickOnItemSearchHighlights(userId: String) {
        tracker.sendGeneralEvent(DataLayer.mapOf(
                EVENT, CLICK_SEARCH,
                EVENT_CATEGORY, GLOBAL_SEARCH,
                EVENT_ACTION, SUGGESTED_SEARCH,
                EVENT_LABEL, "",
                BUSINESS_UNIT, PHYSICAL_GOODS,
                CURRENT_SITE, TOKOPEDIA_SELLER,
                USER_ID, userId
        ))
    }
}