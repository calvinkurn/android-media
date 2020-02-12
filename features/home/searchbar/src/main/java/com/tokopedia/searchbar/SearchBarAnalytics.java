package com.tokopedia.searchbar;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;

/**
 * Created by meta on 04/08/18.
 */
public class SearchBarAnalytics {

    SearchBarAnalytics(Context context) { }

    public void eventTrackingWishlist(String item, String screenName) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getDataEvent(screenName,
                        SearchBarConstant.CLICK_TOP_NAV,
                        SearchBarConstant.TOP_NAV,
                        String.format("%s %s", SearchBarConstant.CLICK, item)));
    }

    public void eventTrackingNotification(String screenName) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getDataEvent(screenName,
                        SearchBarConstant.CLICK_TOP_NAV,
                        SearchBarConstant.TOP_NAV,
                        String.format("%s %s", SearchBarConstant.CLICK,
                                SearchBarConstant.NOTIFICATION)));
    }

    public void eventTrackingNotifCenter() {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(dataNotifCenter());
    }

    private Map<String, Object> dataNotifCenter() {
        Map<String, Object> trackerMap = new HashMap<>();
        trackerMap.put(SearchBarConstant.EVENT, SearchBarConstant.CLICK_NOTIF_CENTER);
        trackerMap.put(SearchBarConstant.EVENT_CATEGORY, SearchBarConstant.NOTIF_CENTER);
        trackerMap.put(SearchBarConstant.EVENT_ACTION, SearchBarConstant.NOTIF_CENTER_ACTION);
        trackerMap.put(SearchBarConstant.EVENT_LABEL, "");
        return trackerMap;
    }

    private Map<String, Object> getDataEvent(String screenName, String event,
                                             String category, String action) {
        Map<String, Object> eventTracking = new HashMap<>();
        eventTracking.put(SearchBarConstant.SCREEN_NAME, screenName);
        eventTracking.put(SearchBarConstant.EVENT, event);
        eventTracking.put(SearchBarConstant.EVENT_CATEGORY, category);
        eventTracking.put(SearchBarConstant.EVENT_ACTION, action);
        eventTracking.put(SearchBarConstant.EVENT_LABEL, "");
        return eventTracking;
    }

    public void eventTrackingSearchBar(String screenName) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                SearchBarConstant.CLICK_TOP_NAV,
                SearchBarConstant.TOP_NAV + " - " + screenName,
                SearchBarConstant.CLICK_SEARCH_BOX,
                ""
        ));
    }
}
