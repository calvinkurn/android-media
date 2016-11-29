package com.tokopedia.core.analytics;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.nishikino.Nishikino;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.router.home.HomeFragmentRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.session.RegisterNewNextFragment;
import com.tokopedia.core.session.RegisterThirdFragment;
import com.tokopedia.core.util.SessionHandler;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.tokopedia.core.analytics.AppScreen.SCREEN_HOME_PRODUCT_CATEGORY;

/**
 * @author  by alvarisi on 9/27/16.
 * Modified by Hafizh Herdi
 */

public class TrackingUtils extends TrackingConfig {

    public static int PARENT_HOME_ACTIVITY = 101;
    public static int TYPE_FRAGMENT_INDEX_CATEGORY = 203;

    public static void eventCampaign(Campaign campaign){
        getGTMEngine()
                .sendCampaign(campaign)
                .clearCampaign(campaign);
    }


//    public static void activityBasedAFEvent(Activity activity){
//        Map<String, Object> afValue = new HashMap<>();
//
//        getAFEngine().sendTrackEvent(AppScreen.convertAFActivityEvent(activity), afValue);
//    }

    //TODO please implement another best practice to do this
    public static void activityBasedAFEvent(int typeActivity) {
        Map<String, Object> afValue = new HashMap<>();
        String eventName = null;
        Activity activity = null;
        if (typeActivity == PARENT_HOME_ACTIVITY) {
            afValue.put(AFInAppEventParameterName.PARAM_1,
                    CommonUtils.getUniqueDeviceID(MainApplication.getAppContext()));
            activity = HomeRouter.getHomeActivity();
            eventName = AFInAppEventType.LOGIN;
        }

        if (activity != null) {
            getAFEngine().sendTrackEvent(eventName, afValue);
        }
    }


    public static void fragmentBasedAFEvent(android.support.v4.app.Fragment fragment){
        Map<String, Object> afValue = new HashMap<>();
        if (fragment instanceof RegisterNewNextFragment || fragment instanceof RegisterThirdFragment){
            afValue.put(AFInAppEventParameterName.REGSITRATION_METHOD,"register_normal");
        }
        getAFEngine().sendTrackEvent(AppScreen.convertAFFragmentEvent(fragment), afValue);
    }

    public static void fragmentBasedAFEvent(Context context, int fragmentType) {
        Map<String, Object> afValue = new HashMap<>();
        android.support.v4.app.Fragment fragment = null;
        String eventname;
        if (fragmentType == TYPE_FRAGMENT_INDEX_CATEGORY) {
            afValue.put(AFInAppEventParameterName.DESCRIPTION, Jordan.AF_SCREEN_HOME_MAIN);
            fragment = HomeFragmentRouter.getHomeFragment(context);
            eventname = SCREEN_HOME_PRODUCT_CATEGORY;
        }
        if (fragment != null) {
            getAFEngine().sendTrackEvent(AppScreen.convertAFFragmentEvent(fragment), afValue);
        }
    }

    public static String eventHTTP(){
        return getGTMEngine().eventHTTP();
    }

    public static void eventAuthenticateLogin(Authenticated authenticated){
        getGTMEngine()
                .eventAuthenticate(authenticated)
                .sendScreen(Authenticated.KEY_CD_NAME);
    }

    public static void eventError(String className, String errorMessage){
        getGTMEngine()
                .eventError(className, errorMessage);
    }

    public static void eventLogAnalytics(String className, String errorMessage){
        getGTMEngine()
                .eventLogAnalytics(className, errorMessage);
    }

    public static void eventOnline(){
        getGTMEngine()
                .eventOnline(SessionHandler.getLoginID(MainApplication.getAppContext()));
    }

    public static void eventPushUserID(){
        getGTMEngine()
                .pushUserId(SessionHandler.getLoginID(MainApplication.getAppContext()));
    }

    public static void eventNetworkError(String error){
        getGTMEngine().eventNetworkError(error);
    }

    static void eventAppsFlyerViewListingSearch(JSONArray productsId, String keyword, ArrayList<String> prodIds) {
        Map<String, Object> listViewEvent = new HashMap<>();
        listViewEvent.put(AFInAppEventParameterName.CONTENT_ID, prodIds);
        listViewEvent.put(AFInAppEventParameterName.CURRENCY, "IDR");
        listViewEvent.put(AFInAppEventParameterName.CONTENT_TYPE, Jordan.AF_VALUE_PRODUCTTYPE);
        listViewEvent.put(AFInAppEventParameterName.SEARCH_STRING, keyword);
        if (productsId.length() > 0) {
            listViewEvent.put(AFInAppEventParameterName.SUCCESS, "success");
        } else {
            listViewEvent.put(AFInAppEventParameterName.SUCCESS, "fail");
        }

        getAFEngine().sendTrackEvent(AFInAppEventType.SEARCH, listViewEvent);
    }

    static void eventAppsFlyerContentView(JSONArray productsId, String keyword, ArrayList<String> prodIds) {
        Map<String, Object> listViewEvent = new HashMap<>();
        listViewEvent.put(AFInAppEventParameterName.CONTENT_ID, prodIds);
        listViewEvent.put(AFInAppEventParameterName.CURRENCY, "IDR");
        listViewEvent.put(AFInAppEventParameterName.CONTENT_TYPE, Jordan.AF_VALUE_PRODUCTTYPE);
        listViewEvent.put(AFInAppEventParameterName.SEARCH_STRING, keyword);
        if (productsId.length() > 0) {
            listViewEvent.put(AFInAppEventParameterName.SUCCESS, "success");
        } else {
            listViewEvent.put(AFInAppEventParameterName.SUCCESS, "fail");
        }

        getAFEngine().sendTrackEvent(AFInAppEventType.CONTENT_VIEW, listViewEvent);
    }

    public static void eventLocaNotificationCallback(Intent intent){
        getLocaEngine().sendNotificationCallback(intent);
    }

    public static void eventLocaNotificationReceived(Bundle data){
        getLocaEngine().sendReceiveNotification(data);
    }

    public static void eventLocaNotification(String eventName, Map<String, String> params){
        eventLoca(eventName, params);
        eventLocaInAppMessaging(eventName);
    }

    public static void eventLoca(String eventName){
        getLocaEngine().tagEvent(eventName);
    }

    public static void eventLoca(String eventName, Map<String, String> params){
        getLocaEngine().tagEvent(eventName, params);
    }

    static void eventLoca(String eventName, Map<String, String> params, long value){
        getLocaEngine().tagEvent(eventName, params, value);
    }

    public static void eventLocaUserAttributes(String loginID, String username, String email){
        getLocaEngine().tagUserAttributes(loginID, username, email);
    }

    public static void eventLocaInApp(String eventName){
        getLocaEngine().triggerInAppMessage(eventName);
    }

    public static void eventLocaInAppMessaging(String eventName){
        getLocaEngine().tageEventandInApp(eventName);
    }

    static void sendGTMEvent(Map<String, Object> dataLayers){
        getGTMEngine().sendEvent(dataLayers);
    }

    public static String getGtmString(String key) {
        return getGTMEngine().getString(key);
    }

    public static boolean getBoolean(String key) {
        return getGTMEngine().getBoolean(key);
    }

    public static long getLong(String key) {
        return getGTMEngine().getLong(key);
    }

    public static double getDouble(String key) {
        return getGTMEngine().getDouble(key);
    }

    // TODO alvarisi true caller
    public static void eventTrueCaller(String userId) {
        /*Nishikino.init(MainApplication.getAppContext()).startAnalytics()
                .sendButtonClick(
                        AppEventTracking.Event.TRUECALLER,
                        AppEventTracking.Category.TRUECALLER,
                        AppEventTracking.Action.INSTALLED,
                        userId
                );*/
    }
}

