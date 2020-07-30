package com.tokopedia.core.gcm.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

import timber.log.Timber;

/**
 * @author  by alvarisi on 1/9/17.
 */

public class ActivitiesLifecycleCallbacks {
    private final Application application;
    private final Context context;
    private Activity liveActivityOrNull;
    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks;

    public ActivitiesLifecycleCallbacks(Application application) {
        this.application = application;
        context = application.getApplicationContext();
        registerActivityLifeCycle();
    }

    private void registerActivityLifeCycle() {
        if (activityLifecycleCallbacks != null) application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);

        activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
            @Override public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                Timber.d("onActivityCreated " + activity.getLocalClassName());
                liveActivityOrNull = activity;
            }

            @Override public void onActivityStarted(Activity activity) {
                Timber.d("onActivityStarted " + activity.getLocalClassName());
            }

            @Override public void onActivityResumed(Activity activity) {
                liveActivityOrNull = activity;
                Timber.d("onActivityResumed " + activity.getLocalClassName());
            }

            @Override public void onActivityPaused(Activity activity) {
                liveActivityOrNull = null;
                Timber.d("onActivityPaused " + activity.getLocalClassName());
            }

            @Override public void onActivityStopped(Activity activity) {
                Timber.d("onActivityStopped " + activity.getLocalClassName());
            }

            @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                Timber.d("onActivitySaveInstanceState " + activity.getLocalClassName());
            }

            @Override public void onActivityDestroyed(Activity activity) {
                Timber.d("onActivityDestroyed " + activity.getLocalClassName());
            }
        };

        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    @Nullable
    public Activity getLiveActivityOrNull() {
        return liveActivityOrNull;
    }

    public boolean isAppOnBackground() {
        return getLiveActivityOrNull() == null;
    }

    public Context getContext(){
        return context;
    }
}
