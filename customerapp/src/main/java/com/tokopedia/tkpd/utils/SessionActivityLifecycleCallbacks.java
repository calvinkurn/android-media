package com.tokopedia.tkpd.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.tokopedia.tkpd.timber.UserIdChangeCallback;
import com.tokopedia.user.session.UserSession;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class SessionActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private static final String TIME_FORMAT = "%.2f";
    private static final long INTERVAL_SESSION = TimeUnit.MINUTES.toMillis(1);
    private long lastSessionMillis = -1;
    private long sessionTime = 0;

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        // No-op
    }

    @Override
    public void onActivityStarted(Activity activity) {
        // No-op
    }

    @Override
    public void onActivityResumed(Activity activity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                logSession(activity.getClass().getSimpleName());
            }
        }).start();
    }

    @Override
    public void onActivityPaused(Activity activity) {
        // No-op
    }

    @Override
    public void onActivityStopped(Activity activity) {
        // No-op
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        // No-op
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        // No-op
    }

    private void logSession(String activityName) {
        long currentMillis = System.currentTimeMillis();
        long minSessionTimeMillis = currentMillis - INTERVAL_SESSION;
        if (lastSessionMillis < minSessionTimeMillis) {
            Timber.w("P1#ACTIVE_SESSION#active;time=%s;diff=%s;cls_name='%s'", ++sessionTime, getDiffDuration(lastSessionMillis, currentMillis), activityName);
            lastSessionMillis = System.currentTimeMillis();
        }
    }

    private String getDiffDuration(long startDuration, long stopDuration) {
        float diffTimeInMillis = 0;
        if (startDuration > 0 && startDuration < stopDuration) {
            diffTimeInMillis = (stopDuration - startDuration);
            diffTimeInMillis /= INTERVAL_SESSION;
        }
        return String.format(Locale.ENGLISH, TIME_FORMAT, diffTimeInMillis);
    }
}
