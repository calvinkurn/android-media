package com.tokopedia.notifications.inApp.viewEngine;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import com.tokopedia.iris.Iris;
import com.tokopedia.iris.IrisAnalytics;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.inApp.CmActivityLifecycleHandler;
import com.tokopedia.notifications.utils.NotificationCancelManager;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * @author lalit.singh
 */
public class CMActivityLifeCycle implements Application.ActivityLifecycleCallbacks {

    public static final String IRIS_ANALYTICS_APP_SITE_OPEN = "appSiteOpen";
    private static final String IRIS_ANALYTICS_EVENT_KEY = "event";
    private final CmActivityLifecycleHandler lifecycleHandler;
    private final NotificationCancelManager cancelManager;

    private int activityCount;

    public CMActivityLifeCycle(CmActivityLifecycleHandler lifecycleHandler) {
        this.lifecycleHandler = lifecycleHandler;
        cancelManager = new NotificationCancelManager();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        try {
            if (activity != null && activityCount == 0) {
                trackIrisEventForAppOpen(activity);
            }
            activityCount++;
            lifecycleHandler.onActivityCreatedInternalForPush(activity);
        } catch (Exception e) {
            Map<String, String> messageMap = new HashMap<>();
            messageMap.put("type", "exception");
            messageMap.put("err", Log.getStackTraceString
                            (e).substring(0, (Math.min(Log.getStackTraceString(e).length(), CMConstant.TimberTags.MAX_LIMIT))));
            messageMap.put("data", "");
            ServerLogger.log(Priority.P2, "CM_VALIDATION", messageMap);
        }
    }

    @Override
    public void onActivityStarted(@NotNull Activity activity) {
        try {
            lifecycleHandler.onActivityStartedInternal(activity);

            if (cancelManager != null && cancelManager.isCancellable(activity)) {
                cancelManager.clearNotifications(activity.getApplicationContext());
            }
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

}
