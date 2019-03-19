package com.tokopedia.searchbar;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.track.interfaces.Analytics;
import com.tokopedia.track.interfaces.ContextAnalytics;

/**
 * Created by meta on 04/08/18.
 */
public class SearchBarAnalytics {

    SearchBarAnalytics(Context context) {

    }

    public void eventTrackingSqanQr() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                SearchBarConstant.CLICK_HOME_PAGE,
                SearchBarConstant.TOP_NAV,
                String.format("%s %s", SearchBarConstant.CLICK,
                        SearchBarConstant.SCAN_QR),
                ""
        ));
    }

    public void eventTrackingWishlist(String item, String screenName) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                getDataEvent(screenName,
                        SearchBarConstant.CLICK_TOP_NAV,
                        SearchBarConstant.TOP_NAV,
                        String.format("%s %s", SearchBarConstant.CLICK, item)));
    }

    public void eventTrackingNotification(String screenName) {
        TrackApp.getInstance().getGTM().sendEnhanceECommerceEvent(
                getDataEvent(screenName,
                        SearchBarConstant.CLICK_HOME_PAGE,
                        SearchBarConstant.TOP_NAV,
                        String.format("%s %s", SearchBarConstant.CLICK,
                                SearchBarConstant.NOTIFICATION)));
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

    public void eventTrackingSearchBar() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(TrackAppUtils.gtmData(
                SearchBarConstant.CLICK_TOP_NAV,
                SearchBarConstant.TOP_NAV,
                SearchBarConstant.CLICK_SEARCH_BOX,
                ""
        ));
    }
}
