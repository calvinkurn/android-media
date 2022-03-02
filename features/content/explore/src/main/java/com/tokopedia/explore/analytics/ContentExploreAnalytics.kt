package com.tokopedia.explore.analytics

import com.tokopedia.feedcomponent.analytics.tracker.FeedAnalyticTracker
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import javax.inject.Inject

/**
 * @author by yoasfs on 2020-01-08
 */
class ContentExploreAnalytics @Inject constructor() {


    fun sendScreenName(screenName: String) {
        TrackApp.getInstance().gtm.sendScreenAuthenticated(screenName)
    }

    fun eventTrackExploreItem(name : String, postId: Int, recomId: Int) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                ContentExloreEventTracking.Event.EXPLORE,
                ContentExloreEventTracking.Category.EXPLORE_INSPIRATION,
                ContentExloreEventTracking.Action.CLICK_GRID_CONTENT,
                String.format(
                        ContentExloreEventTracking.EventLabel.CLICK_GRID_CONTENT_LABEL,
                        name,
                        postId,
                        recomId
                )
        ))
    }
    fun visitExplorePageOnFeed(isLoggedInStatus: String){
        val map = mapOf(
                TrackAppUtils.EVENT to ContentExloreEventTracking.Event.OPEN_SCREEN,
                ContentExloreEventTracking.Event.IS_LOGGED_IN to isLoggedInStatus,
                ContentExloreEventTracking.Event.SCREEN_NAME to ContentExloreEventTracking.Screen.SCREEN_NAME,
        )
        TrackApp.getInstance().gtm.sendGeneralEvent(map)
    }

    fun eventSubmitSearch(text: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                ContentExloreEventTracking.Event.EXPLORE,
                ContentExloreEventTracking.Category.EXPLORE_INSPIRATION,
                ContentExloreEventTracking.Action.SEARCH,
                text
        ))
    }

    fun eventImpressionSuccessGetData() {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                ContentExloreEventTracking.Event.EXPLORE,
                ContentExloreEventTracking.Category.EXPLORE_INSPIRATION,
                ContentExloreEventTracking.Action.LOAD_MORE,
                ""
        ))
    }

    fun eventDeselectCategory(categoryName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                ContentExloreEventTracking.Event.EXPLORE,
                ContentExloreEventTracking.Category.EXPLORE_INSPIRATION,
                ContentExloreEventTracking.Action.DESELECT_CATEGORY,
                categoryName
        ))
    }

    fun eventSelectCategory(categoryName: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(
                ContentExloreEventTracking.Event.EXPLORE,
                ContentExloreEventTracking.Category.EXPLORE_INSPIRATION,
                ContentExloreEventTracking.Action.FILTER_CATEGORY,
                categoryName
        ))
    }

}