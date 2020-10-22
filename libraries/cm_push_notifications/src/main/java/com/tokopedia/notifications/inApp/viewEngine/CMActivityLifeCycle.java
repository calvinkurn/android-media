package com.tokopedia.notifications.inApp.viewEngine;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.tokopedia.iris.Iris;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.notifications.inApp.CmActivityLifecycleHandler;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * @author lalit.singh
 */
public class CMActivityLifeCycle implements Application.ActivityLifecycleCallbacks {

    static String TAG = CMActivityLifeCycle.class.getSimpleName();
    public static final String IRIS_ANALYTICS_APP_SITE_OPEN = "appSiteOpen";
    private static final String IRIS_ANALYTICS_EVENT_KEY = "event";
    private int activityCount;
    private CmActivityLifecycleHandler lifecycleHandler;


    public CMActivityLifeCycle(CmActivityLifecycleHandler lifecycleHandler) {
        this.lifecycleHandler = lifecycleHandler;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity != null && activityCount == 0) {
            trackIrisEventForAppOpen(activity);
        }
        activityCount++;
        try {
            lifecycleHandler.onActivityCreatedInternalForPush(activity);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        try {
            lifecycleHandler.onActivityStartedInternal(activity);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    private void trackIrisEventForAppOpen(Activity activity) {
        Iris instance = IrisAnalytics.Companion.getInstance(activity);
        Map<String, Object> map = new HashMap<>();
        map.put(IRIS_ANALYTICS_EVENT_KEY, IRIS_ANALYTICS_APP_SITE_OPEN);
        instance.saveEvent(map);
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
        try {
            lifecycleHandler.onActivityStopInternal(activity);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }


    @Override
    public void onActivityDestroyed(Activity activity) {
        activityCount--;
        try {
            lifecycleHandler.onActivityDestroyedInternal(activity);
        } catch (Exception e) {
            Timber.e(e);
        }

    }


}
