package com.tokopedia.filter.newdynamicfilter.analytics;


public class DiscoveryTrackingFactory {
    private static FilterTracking searchTracking;
    private static CategoryPageAnalytics categoryPageAnalytics;
    public static final int STATE_TRACKING_CATEGORY = 1;
    public static final int STATE_TRACKING_SEARCH = 2;

    public static int TRACKING_STATE = STATE_TRACKING_SEARCH;
    public static FilterTracking getmInstance() {

        if(TRACKING_STATE == STATE_TRACKING_CATEGORY) {
            if(categoryPageAnalytics == null) {
                categoryPageAnalytics = new CategoryPageAnalytics();
            }
            return categoryPageAnalytics;
        }

        if(searchTracking == null) {
            searchTracking = new FilterTracking();
        }
        return searchTracking;
    }

    public static void setTrackingState(int trackingState) {
        TRACKING_STATE = trackingState;
    }

    public static void setDefaultState() {
        TRACKING_STATE = STATE_TRACKING_SEARCH;
    }
}
