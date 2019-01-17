package com.tokopedia.core.gcm.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tkpd.library.utils.legacy.CommonUtils;

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
                CommonUtils.dumper("onActivityCreated " + activity.getLocalClassName());
                liveActivityOrNull = activity;
            }

            @Override public void onActivityStarted(Activity activity) {
                CommonUtils.dumper("onActivityStarted " + activity.getLocalClassName());
            }

            @Override public void onActivityResumed(Activity activity) {
                liveActivityOrNull = activity;
                CommonUtils.dumper("onActivityResumed " + activity.getLocalClassName());
            }

            @Override public void onActivityPaused(Activity activity) {
                liveActivityOrNull = null;
                CommonUtils.dumper("onActivityPaused " + activity.getLocalClassName());
            }

            @Override public void onActivityStopped(Activity activity) {
                CommonUtils.dumper("onActivityStopped " + activity.getLocalClassName());
            }

            @Override public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                CommonUtils.dumper("onActivitySaveInstanceState " + activity.getLocalClassName());
            }

            @Override public void onActivityDestroyed(Activity activity) {
                CommonUtils.dumper("onActivityDestroyed " + activity.getLocalClassName());
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

    Application getApplication() {
        return application;
    }

    public Context getContext(){
        return context;
    }
}
