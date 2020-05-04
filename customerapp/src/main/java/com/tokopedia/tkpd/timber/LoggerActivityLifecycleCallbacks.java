package com.tokopedia.tkpd.timber;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.tokopedia.user.session.UserSession;

public class LoggerActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private String userId = "";

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        UserSession userSession = new UserSession(activity);
        if (!userId.equals(userSession.getUserId())) {
            userId = userSession.getUserId();
            TimberWrapper.initConfig(activity.getApplication());
        }
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