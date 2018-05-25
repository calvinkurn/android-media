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

    public static void hamburgerIconClickCategory(String landingScreen, String optionName) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        map.put(FirebaseParams.Home.OPTION_NAME, landingScreen);

        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_OPTION_CLICK, map);
    }
    public static void hamburgerOptionClicked(String landingScreen, String optionName) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        map.put(FirebaseParams.Home.OPTION_NAME, optionName);

        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_OPTION_CLICK, map);
    }
    public static void hamburgerOptionClicked(String landingScreen, String optionName, String subCategory) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        map.put(FirebaseParams.Home.OPTION_NAME, optionName);
        map.put(FirebaseParams.Home.SUBCATEGORY_NAME, subCategory);

        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_OPTION_CLICK, map);
    }

    public static void hamburgerIconClickLogin(String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_ICON_CLICK_LOGIN, map);
    }

    public static void hamburgerIconClickSignup(String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_ICON_CLICK_SIGNUP, map);
    }
    public static void hamburgerTokocashActivateClick() {
        Map<String, Object> map = new HashMap<>();
        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_TOKOCASH_ACTIVATE, map);
    }

    public static void homepageTokocashClick(String landingScreen ) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_TOKOCASH, map);
    }

    public static void hamburgerTokopointsClick(String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_TOKOPOINTS, map);
    }

    public static void homepageSaldoClick(String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_SALDO, map);
    }

    public static void hambugerProfileClick(String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_PROFILE, map);
    }


    public static void sendEventToAnalytics(String eventName, Map<String, Object> data){
         TrackAnalytics.sendEvent(eventName,data, MainApplication.getAppContext());
    }
}
