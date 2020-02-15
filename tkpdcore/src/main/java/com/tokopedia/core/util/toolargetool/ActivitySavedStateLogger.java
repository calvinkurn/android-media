package com.tokopedia.core.util.toolargetool;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.crashlytics.android.Crashlytics;

import java.util.WeakHashMap;

/**
 * {@link android.app.Application.ActivityLifecycleCallbacks} implementation that logs information
 * about the saved state of Activities.
 */
public class ActivitySavedStateLogger implements Application.ActivityLifecycleCallbacks {

    private final int priority;
    @NonNull
    private final String tag;
    @Nullable
    private final FragmentSavedStateLogger fragmentLogger;
    @NonNull
    private final WeakHashMap<Activity, Bundle> savedStates = new WeakHashMap<>();

    public ActivitySavedStateLogger(int priority, @NonNull String tag, boolean logFragments) {
        this.priority = priority;
        this.tag = tag;
        fragmentLogger = logFragments ? new FragmentSavedStateLogger(priority, tag) : null;
    }

    private void log(String msg) {
        Log.println(priority, tag, msg);
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        if (activity instanceof FragmentActivity && fragmentLogger != null) {
            ((FragmentActivity) activity)
                    .getSupportFragmentManager()
                    .registerFragmentLifecycleCallbacks(fragmentLogger, true);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (activity instanceof FragmentActivity && fragmentLogger != null) {
            ((FragmentActivity) activity)
                    .getSupportFragmentManager()
                    .unregisterFragmentLifecycleCallbacks(fragmentLogger);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        savedStates.put(activity, outState);
    }

    @Override
    public void onActivityStopped(Activity activity) {
        Bundle savedState = savedStates.remove(activity);
        if (savedState != null) {
            String message = activity.getClass().getSimpleName() + ".onSaveInstanceState wrote: " + TooLargeTool.bundleBreakdown(savedState);
            log(message);
            if (TooLargeTool.isPotentialCrash(savedState) && !com.tokopedia.config.GlobalConfig.DEBUG)
                Crashlytics.logException(new Throwable(message));
        }
    }
}
