package com.tokopedia.core.analytics;

import android.content.Context;

import com.tokopedia.analytics.TrackAnalytics;
import com.tokopedia.analytics.firebase.FirebaseEvent;
import com.tokopedia.analytics.firebase.FirebaseParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ashwanityagi on 25/03/18.
 */

public class AnalyticsEventTrackingHelper {

    public static void homepageSaldoClick(Context context, String landingScreen) {
        Map<String, Object> map = new HashMap<>();
        map.put(FirebaseParams.Home.LANDING_SCREEN_NAME, landingScreen);
        sendEventToAnalytics(context, FirebaseEvent.Home.HAMBURGER_SALDO, map);
    }

    public static void sendEventToAnalytics(Context context, String eventName, Map<String, Object> data){
        TrackAnalytics.sendEvent(eventName,data, context);
    }
}
