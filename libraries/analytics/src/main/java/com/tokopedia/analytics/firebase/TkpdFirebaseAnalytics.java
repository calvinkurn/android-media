package com.tokopedia.analytics.firebase;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;

import org.jetbrains.annotations.Nullable;

/**
 * Created by ashwanityagi on 14/03/18.
 */

public class TkpdFirebaseAnalytics {

    private static final Object lock = new Object();

    @SuppressLint("StaticFieldLeak")
    private static TkpdFirebaseAnalytics analytics;

    @ApplicationContext
    private Context context;

    private TkpdFirebaseAnalytics() {
    }


    public static TkpdFirebaseAnalytics getInstance(@NonNull Context context) {
        if (analytics == null) {
            synchronized (lock) {
                if (analytics == null) {
                    analytics = new TkpdFirebaseAnalytics();
                    analytics.context = context.getApplicationContext();
                }
            }
        }
        return analytics;
    }

    public void logEvent(String eventName, Bundle bundle) {
        try {
            FirebaseAnalytics.getInstance(context).logEvent(eventName, bundle);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setUserId(@Nullable String userId) {
        try {
            FirebaseAnalytics.getInstance(context).setUserId(userId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}