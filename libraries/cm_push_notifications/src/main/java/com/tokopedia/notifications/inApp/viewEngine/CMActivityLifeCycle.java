package com.tokopedia.notifications.inApp.viewEngine;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.tokopedia.iris.Iris;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.notifications.inApp.CmActivityLifecycleHandler;
import com.tokopedia.notifications.utils.NotificationCancelManager;
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.remoteconfig.RemoteConfigKey;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * @author lalit.singh
 */
public class CMActivityLifeCycle implements Application.ActivityLifecycleCallbacks {

    public static final String IRIS_ANALYTICS_APP_SITE_OPEN = "appSiteOpen";
    private static final String IRIS_ANALYTICS_EVENT_KEY = "event";
    private int activityCount;

    private CmActivityLifecycleHandler lifecycleHandler;
    private NotificationCancelManager cancelManager;
    private RemoteConfig remoteConfig;

    public CMActivityLifeCycle(Context context, CmActivityLifecycleHandler lifecycleHandler) {
        cancelManager = new NotificationCancelManager(context);
        remoteConfig = new FirebaseRemoteConfigImpl(context);

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
            clearNotification(activity);
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) { }

    @Override
    public void onActivityPaused(Activity activity) { }

    @Override
    public void onActivityStopped(Activity activity) {
        try {
            lifecycleHandler.onActivityStopInternal(activity);
            cancelManager.cancel();
        } catch (Exception e) {
            Timber.e(e);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) { }

    @Override
    public void onActivityDestroyed(Activity activity) {
        activityCount--;
        try {
            lifecycleHandler.onActivityDestroyedInternal(activity);
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

    private void clearNotification(Activity activity) {
        if (remoteConfig.getBoolean(RemoteConfigKey.NOTIFICATION_TRAY_CLEAR)) {
            cancelManager.clearNotifications(activity.getApplicationContext());
        }
    }

}
