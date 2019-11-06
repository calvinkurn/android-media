package com.tokopedia.analytics;

import android.content.Context;
import android.os.Bundle;

import com.tokopedia.analytics.firebase.TkpdFirebaseAnalytics;

import java.util.Map;

/**
 * Created by ashwanityagi on 20/03/18.
 */

public class TrackAnalytics {

    public static void sendEvent(String eventName, Map<String, Object> data, Context context) {
        sendEventToFirebase(eventName, data, context);
    }

    private static void sendEventToFirebase(String eventName, Map<String, Object> data, Context context) {
        if (context != null)
            TkpdFirebaseAnalytics.getInstance(context).logEvent(eventName, convertMapToBundle(data));
    }

    private static Bundle convertMapToBundle(Map<String, Object> data) {
        Bundle bundle = new Bundle();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            bundle.putString(entry.getKey(), String.valueOf(entry.getValue()));
        }
        return bundle;
    }
}