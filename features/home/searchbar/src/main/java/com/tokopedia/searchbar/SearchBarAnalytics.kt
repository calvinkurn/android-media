package com.tokopedia.searchbar

import android.content.Context
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.iris.util.KEY_SESSION_IRIS
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import java.util.*

/**
 * Created by DevAra on 07/10/20.
 */
class SearchBarAnalytics internal constructor(context: Context?) {
    private val irisSession: IrisSession = IrisSession(context!!)
    private val SEARCH_BAR_EVENT_CATEGORY_FORMAT = "%s - %s"
    fun eventTrackingWishlist(item: String?, screenName: String?) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDataEvent(screenName,
                        SearchBarConstant.CLICK_TOP_NAV, String.format(SEARCH_BAR_EVENT_CATEGORY_FORMAT, SearchBarConstant.TOP_NAV, screenName), String.format("%s %s", SearchBarConstant.CLICK, item)))
    }

    fun eventTrackingNotification(screenName: String?) {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(
                getDataEvent(screenName,
                        SearchBarConstant.CLICK_TOP_NAV, String.format(SEARCH_BAR_EVENT_CATEGORY_FORMAT, SearchBarConstant.TOP_NAV, screenName), String.format("%s %s", SearchBarConstant.CLICK,
                        SearchBarConstant.NOTIFICATION)))
    }

    fun eventTrackingNotifCenter() {
        TrackApp.getInstance().gtm.sendEnhanceEcommerceEvent(dataNotifCenter())
    }

    private fun dataNotifCenter(): Map<String, Any> {
        val trackerMap: MutableMap<String, Any> = HashMap()
        trackerMap[SearchBarConstant.EVENT] = SearchBarConstant.CLICK_NOTIF_CENTER
        trackerMap[SearchBarConstant.EVENT_CATEGORY] = SearchBarConstant.NOTIF_CENTER
        trackerMap[SearchBarConstant.EVENT_ACTION] = SearchBarConstant.NOTIF_CENTER_ACTION
        trackerMap[SearchBarConstant.EVENT_LABEL] = ""
        return trackerMap
    }

    private fun getDataEvent(screenName: String?, event: String,
                             category: String, action: String): Map<String, Any?> {
        val eventTracking: MutableMap<String, Any?> = HashMap()
        eventTracking[SearchBarConstant.SCREEN_NAME] = screenName
        eventTracking[SearchBarConstant.EVENT] = event
        eventTracking[SearchBarConstant.EVENT_CATEGORY] = category
        eventTracking[SearchBarConstant.EVENT_ACTION] = action
        eventTracking[SearchBarConstant.EVENT_LABEL] = ""
        return eventTracking
    }

    fun eventTrackingSearchBar(screenName: String?, keyword: String?) {
        val stringObjectMap = TrackAppUtils.gtmData(
                SearchBarConstant.CLICK_TOP_NAV, String.format(SEARCH_BAR_EVENT_CATEGORY_FORMAT, SearchBarConstant.TOP_NAV, screenName),
                SearchBarConstant.CLICK_SEARCH_BOX,
                keyword
        )
        stringObjectMap[KEY_SESSION_IRIS] = irisSession.getSessionId()
        TrackApp.getInstance().gtm.sendGeneralEvent(stringObjectMap)
    }

}