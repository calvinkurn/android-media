package com.tokopedia.searchbar;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;
import com.tokopedia.track.TrackApp;
import com.tokopedia.track.TrackAppUtils;
import com.tokopedia.iris.util.IrisSession;
import com.tokopedia.iris.util.ConstantKt;

/**
 * Created by meta on 04/08/18.
 */
public class SearchBarAnalytics {

    private final IrisSession irisSession;

    private String SEARCH_BAR_EVENT_CATEGORY_FORMAT = "%s - %s";

    SearchBarAnalytics(Context context) {
        irisSession = new IrisSession(context);
    }

    public void eventTrackingWishlist(String item, String screenName) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getDataEvent(screenName,
                        SearchBarConstant.CLICK_TOP_NAV,
                        String.format(SEARCH_BAR_EVENT_CATEGORY_FORMAT, SearchBarConstant.TOP_NAV, screenName),
                        String.format("%s %s", SearchBarConstant.CLICK, item)));
    }

    public void eventTrackingNotification(String screenName) {
        TrackApp.getInstance().getGTM().sendEnhanceEcommerceEvent(
                getDataEvent(screenName,
                        SearchBarConstant.CLICK_TOP_NAV,
                        String.format(SEARCH_BAR_EVENT_CATEGORY_FORMAT, SearchBarConstant.TOP_NAV, screenName),
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

    public void eventTrackingSearchBar(String screenName, String keyword) {
        Map<String, Object> stringObjectMap = TrackAppUtils.gtmData(
                SearchBarConstant.CLICK_TOP_NAV,
                String.format(SEARCH_BAR_EVENT_CATEGORY_FORMAT, SearchBarConstant.TOP_NAV, screenName),
                SearchBarConstant.CLICK_SEARCH_BOX,
                keyword
        );
        stringObjectMap.put(ConstantKt.KEY_SESSION_IRIS, irisSession.getSessionId());
        TrackApp.getInstance().getGTM().sendGeneralEvent(stringObjectMap);
    }
}
