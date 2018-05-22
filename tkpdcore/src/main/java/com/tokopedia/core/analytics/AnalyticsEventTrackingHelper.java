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
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        bundle.put(FirebaseParams.Home.OPTION_NAME, landingScreen);

        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_OPTION_CLICK, bundle);
    }
    public static void hamburgerOptionClicked(String landingScreen, String optionName) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        bundle.put(FirebaseParams.Home.OPTION_NAME, optionName);

        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_OPTION_CLICK, bundle);
    }
    public static void hamburgerOptionClicked(String landingScreen, String optionName, String subCategory) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        bundle.put(FirebaseParams.Home.OPTION_NAME, optionName);
        bundle.put(FirebaseParams.Home.SUBCATEGORY_NAME, subCategory);

        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_OPTION_CLICK, bundle);
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
    public static void homepageTokocashActivateClick() {
        Map<String, Object> bundle = new HashMap<>();
        sendEventToAnalytics(FirebaseEvent.Home.HOMEPAGE_TOKOCASH_ACTIVATE, bundle);
    }

    public static void homepageTokocashClick() {
        Map<String, Object> bundle = new HashMap<>();
        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_TOKOCASH, bundle);
    }

    public static void homepageTokopointsClick() {
        Map<String, Object> bundle = new HashMap<>();
        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_TOKOPOINTS, bundle);
    }

    public static void homepageSaldoClick() {
        Map<String, Object> bundle = new HashMap<>();
        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_SALDO, bundle);
    }

    public static void hambugerProfileClick(String landingScreen) {
        Map<String, Object> bundle = new HashMap<>();
        bundle.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(FirebaseEvent.Home.HAMBURGER_PROFILE, bundle);
    }


    public static void sendEventToAnalytics(String eventName, Map<String, Object> data){
         TrackAnalytics.sendEvent(eventName,data, MainApplication.getAppContext());
    }
}
