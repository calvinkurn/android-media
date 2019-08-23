package com.tokopedia.travel.homepage.analytics

import com.tokopedia.track.TrackApp
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingActionConstant.BANNER_CLICK_ALL
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingActionConstant.DEALS_CLICK_ALL
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingActionConstant.ORDER_CLICK
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingActionConstant.ORDER_CLICK_ALL
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingActionConstant.POPULAR_SEARCH_CLICK
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingActionConstant.RECENT_SEARCH_CLICK
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingCategoryConstant.TRAVEL_HOMEPAGE_CATEGORY
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingEventNameConstant.CLICK_HOMEPAGE
import com.tokopedia.travel.homepage.analytics.TravelHomepageTrackingLabelConstant.CLICK

/**
 * @author by furqan on 23/08/2019
 */
class TravelHomepageTrackingUtil {

    fun travelHomepageClickAllBanner() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, TRAVEL_HOMEPAGE_CATEGORY, BANNER_CLICK_ALL, CLICK)
    }

    fun travelHomepageClickOrder(position: String, categoryName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, TRAVEL_HOMEPAGE_CATEGORY, ORDER_CLICK, "$position - $categoryName")
    }

    fun travelHomepageClickAllOrder() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, TRAVEL_HOMEPAGE_CATEGORY, ORDER_CLICK_ALL, CLICK)
    }

    fun travelHomepageClickRecentSearch(position: String, categoryName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, TRAVEL_HOMEPAGE_CATEGORY, RECENT_SEARCH_CLICK, "$position - $categoryName")
    }

    fun travelHomepageClickPopularSearch(position: String, categoryName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, TRAVEL_HOMEPAGE_CATEGORY, POPULAR_SEARCH_CLICK, "$position - $categoryName")
    }

    fun travelHomepageClickAllDeals() {
        TrackApp.getInstance().gtm.sendGeneralEvent(CLICK_HOMEPAGE, TRAVEL_HOMEPAGE_CATEGORY, DEALS_CLICK_ALL, CLICK)
    }

}