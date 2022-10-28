package com.tokopedia.notifications.inApp.applifecycle;

import static com.tokopedia.notifications.common.CMRemoteConfigKey.NOTIFICATION_TRAY_CLEAR_V1;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;
import com.tokopedia.notifications.common.CMConstant;
import com.tokopedia.notifications.common.CMRemoteConfigUtils;
import com.tokopedia.notifications.utils.NotificationCancelManager;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/**
 * @author lalit.singh
 */
public class CMActivityLifeCycle implements Application.ActivityLifecycleCallbacks {

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
                lifecycleHandler.onFirstScreenOpen(new WeakReference<>(activity));
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
            Context context = activity.getApplicationContext();
            CMRemoteConfigUtils remoteConfig = new CMRemoteConfigUtils(context);
            boolean willNotificationTrayClear = remoteConfig.getBooleanRemoteConfig(
                    NOTIFICATION_TRAY_CLEAR_V1, false);
            lifecycleHandler.onActivityStartedInternal(activity);
            if (cancelManager.isCancellable(activity) && willNotificationTrayClear) {
                cancelManager.clearNotifications(context);
            }
        } catch (Exception e) {
            Timber.e(e);
        }
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
            cancelManager.cancel();
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
