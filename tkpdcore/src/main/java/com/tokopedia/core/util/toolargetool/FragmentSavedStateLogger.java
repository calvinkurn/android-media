package com.tokopedia.core.util.toolargetool;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import com.crashlytics.android.Crashlytics;
import java.util.WeakHashMap;

/**
 * {@link androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks} implementation that
 * logs information about the saved state of Fragments.
 */
public class FragmentSavedStateLogger extends FragmentManager.FragmentLifecycleCallbacks {

    private final int priority;
    @NonNull
    private final String tag;
    @NonNull
    private final WeakHashMap<Fragment, Bundle> savedStates = new WeakHashMap<>();

    public FragmentSavedStateLogger(int priority, @NonNull String tag) {
        this.priority = priority;
        this.tag = tag;
    }

    private void log(String msg) {
        Log.println(priority, tag, msg);
    }

    @Override
    public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
        savedStates.put(f, outState);
    }

    @Override
    public void onFragmentStopped(FragmentManager fm, Fragment f) {
        Bundle savedState = savedStates.remove(f);
        if (savedState != null) {
            String message = f.getClass().getSimpleName() + ".onSaveInstanceState wrote: " + TooLargeTool.bundleBreakdown(savedState);
            if (f.getArguments() != null) {
                message += "\n* fragment arguments = " + TooLargeTool.bundleBreakdown(f.getArguments());
            }
            if (TooLargeTool.isPotentialCrash(savedState))
                Crashlytics.logException(new Throwable(message));
            log(message);
        }
    }
}
