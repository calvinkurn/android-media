package com.tokopedia.tkpd.timber;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.tokopedia.user.session.UserSession;

public class UserIdSubscriber implements Application.ActivityLifecycleCallbacks {

    private String userId = "";
    private Context appcontext;
    private UserIdChangeCallback callback;

    public UserIdSubscriber(Context context, UserIdChangeCallback callback) {
        this.appcontext = context;
        this.callback = callback;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        UserSession userSession = new UserSession(appcontext);
        if(!userId.equals(userSession.getUserId())) {
            userId = userSession.getUserId();
            callback.onUserIdChanged();
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
