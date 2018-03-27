package com.tokopedia.core.analytics;

import com.tokopedia.analytics.TrackAnalytics;
import com.tokopedia.analytics.firebase.FirebaseEvent;
import com.tokopedia.analytics.firebase.FirebaseParams;
import com.tokopedia.core.app.MainApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ashwanityagi on 25/03/18.
 */

public class AnalyticsEventTrackingHelper {

    public static void hamburgerIconClickCategory(String landingScreen) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_ICON_CLICK_CATEGORY, bundle);
    }

    public static void hamburgerIconClickLogin(String landingScreen) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_ICON_CLICK_LOGIN, bundle);
    }

    public static void hamburgerIconClickSignup(String landingScreen) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_ICON_CLICK_SIGNUP, bundle);
    }


    public static void sendEventToAnalytics(String eventName, Map<String, Object> data){
         TrackAnalytics.sendEvent(eventName,data, MainApplication.getAppContext());
    }
}
