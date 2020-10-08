package com.tokopedia.tkpd.timber;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.tokopedia.user.session.UserSession;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

public class LoggerActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private String userId = "";
    private final String ENABLE_ASYNC_USER_BASED_TIMBER_SESSION= "android_async_user_based_timber_session";
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        WeaveInterface userTimberSession = new WeaveInterface() {
            @NotNull
            @Override
            public Object execute() {
                UserSession userSession = new UserSession(activity);
                if (!userId.equals(userSession.getUserId())) {
                    userId = userSession.getUserId();
                    TimberWrapper.initConfig(activity.getApplication());
                }
                return true;
            }
        };
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(userTimberSession, ENABLE_ASYNC_USER_BASED_TIMBER_SESSION, activity.getApplicationContext());
    }

    @Override
    public void onActivityStarted(Activity activity) {
        // No-op
    }

    @Override
    public void onActivityResumed(Activity activity) {
        // No-op
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
}