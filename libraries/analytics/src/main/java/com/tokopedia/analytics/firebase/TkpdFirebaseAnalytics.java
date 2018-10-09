package com.tokopedia.analytics.firebase;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by ashwanityagi on 14/03/18.
 */

public class TkpdFirebaseAnalytics {

    public static void logEvent(String eventName, Bundle bundle,Context context){
        try {
            FirebaseAnalytics.getInstance(context).logEvent(eventName, bundle);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
