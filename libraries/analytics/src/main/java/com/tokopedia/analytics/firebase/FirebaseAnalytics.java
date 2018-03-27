package com.tokopedia.analytics.firebase;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by ashwanityagi on 14/03/18.
 */

public class FirebaseAnalytics {

    public static void logEvent(String eventName, Bundle bundle,Context context){
        try {
            com.google.firebase.analytics.FirebaseAnalytics.getInstance(context).logEvent(eventName, bundle);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
