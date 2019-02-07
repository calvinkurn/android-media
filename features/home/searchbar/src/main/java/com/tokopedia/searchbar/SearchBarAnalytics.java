package com.tokopedia.searchbar;

import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by meta on 04/08/18.
 */
public class SearchBarAnalytics {

    private AnalyticTracker analyticTracker;

    SearchBarAnalytics(Context context) {
        if (context == null)
            return;

        if (context.getApplicationContext() instanceof AbstractionRouter) {
            this.analyticTracker = ((SearchBarRouter) context.getApplicationContext())
                    .getAnalyticTracker();
        }
    }

    public void eventTrackingSqanQr() {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                SearchBarConstant.CLICK_HOME_PAGE,
                SearchBarConstant.TOP_NAV,
                String.format("%s %s", SearchBarConstant.CLICK,
                        SearchBarConstant.SCAN_QR),
                ""
        );
    }

    public void eventTrackingWishlist(String item, String screenName) {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
                getDataEvent(screenName,
                        SearchBarConstant.CLICK_TOP_NAV,
                        SearchBarConstant.TOP_NAV,
                        String.format("%s %s", SearchBarConstant.CLICK, item)));
    }

    public void eventTrackingNotification(String screenName) {
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(
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
        if (analyticTracker == null)
            return;

        analyticTracker.sendEventTracking(getDataEvent(
                SearchBarConstant.CLICK_TOP_NAV,
                SearchBarConstant.TOP_NAV,
                SearchBarConstant.CLICK_SEARCH_BOX,
                ""
        ));
    }
}
