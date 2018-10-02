package com.tokopedia.kelontongapp;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by meta on 02/10/18.
 */
public class KelontongMainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initCrashlytics();
    }

    public void initCrashlytics() {
        if (!BuildConfig.DEBUG) {
            Fabric.with(this, new Crashlytics());
            Crashlytics.setUserIdentifier("");
        }
    }
}
