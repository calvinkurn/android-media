package com.tokopedia.core.analytics;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.utils.legacy.CommonUtils;
import com.tokopedia.core.analytics.container.GTMAnalytics;
import com.tokopedia.core.analytics.nishikino.model.Campaign;
import com.tokopedia.track.TrackApp;

import java.util.HashMap;
import java.util.Map;

/**
 * @author by alvarisi on 9/27/16.
 * Modified by Hafizh Herdi
 */

@Deprecated
public class TrackingUtils{
    private static final String PARAM_1 = "af_param_1";

    public static void activityBasedAFEvent(Context context, String tag) {
        Map<String, Object> afValue = new HashMap<>();
        if (tag.equals(AppScreen.IDENTIFIER_HOME_ACTIVITY)) {
            afValue.put(PARAM_1, CommonUtils.getUniqueDeviceID(context));
        }
        TrackApp.getInstance().getAppsFlyer().sendTrackEvent(AppScreen.convertAFActivityEvent(tag), afValue);
    }

    public static void sendMoEngageClickMainCategoryIcon(Context context, String categoryName) {
        Map<String, Object> value = DataLayer.mapOf(
                AppEventTracking.MOENGAGE.CATEGORY, categoryName
        );
        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, AppEventTracking.EventMoEngage.CLICK_MAIN_CATEGORY_ICON);
    }

    public static void sendMoEngageReferralShareEvent(Context context,String channel) {
        Map<String, Object> value = DataLayer.mapOf(
                AppEventTracking.MOENGAGE.CHANNEL, channel
        );
        TrackApp.getInstance().getMoEngage().sendTrackEvent(value, AppEventTracking.EventMoEngage.REFERRAL_SHARE_EVENT);
    }

    public static void eventError(Context context,String className, String errorMessage) {
        TrackApp.getInstance().getGTM()
                .eventError(className, errorMessage);
    }

    /**
     * SessionHandler.getGTMLoginID(MainApplication.getAppContext())
     */
    public static void eventOnline(Context context,String uid) {
        TrackApp.getInstance().getGTM()
                .eventOnline(uid);
    }

    public static void sendGTMEvent(Context context, Map<String, Object> dataLayers) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(dataLayers);
    }

    public static void sendAppsFlyerDeeplink(Activity activity) {
        TrackApp.getInstance().getAppsFlyer().sendDeeplinkData(activity);
    }

    public static String getClientID(Context context) {
        return TrackApp.getInstance().getGTM().getClientIDString();
    }

    public static String getAfUniqueId(Context context) {
        return TrackApp.getInstance().getAppsFlyer().getUniqueId();
    }

    public static boolean isValidCampaign(Map<String, Object> maps) {
        boolean isValid = false;

        if(maps != null){
            String gclid = maps.get(AppEventTracking.GTM.UTM_GCLID) != null ? maps.get(AppEventTracking.GTM.UTM_GCLID).toString() : "";
            if(maps.containsKey(AppEventTracking.GTM.UTM_GCLID) && !TextUtils.isEmpty(gclid)){
                isValid = true;
            }else{
                String utmSource = maps.get(AppEventTracking.GTM.UTM_SOURCE) != null ? maps.get(AppEventTracking.GTM.UTM_SOURCE).toString() : "";
                String utmMedium = maps.get(AppEventTracking.GTM.UTM_MEDIUM) != null ? maps.get(AppEventTracking.GTM.UTM_MEDIUM).toString() : "";

                isValid = !TextUtils.isEmpty(utmSource) && !TextUtils.isEmpty(utmMedium);
            }

            if(!isValid){
                Log.d("TrackingUtils", "Invalid Campaign Data = "+maps.toString());
            }else {
                GTMAnalytics.setUTMParamsForSession(maps);
            }
        }

        return isValid;
    }
}