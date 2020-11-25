package com.tokopedia.tkpd.timber;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.tokopedia.logger.utils.WorkManagerPruner;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

public class LoggerActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private String userId = "";
    private static final String ENABLE_ASYNC_USER_BASED_TIMBER_SESSION= "android_async_user_based_timber_session";

    private LoggerWeaverInterface loggerWeaverInterface = null;
    class LoggerWeaverInterface implements WeaveInterface {

        Context appContext;
        UserSession userSession;
        LoggerWeaverInterface(Context context) {
            appContext = context.getApplicationContext();
            userSession = new UserSession(appContext);
        }

        @NotNull
        @Override
        public Object execute() {
            if (!userId.equals(userSession.getUserId())) {
                userId = userSession.getUserId();
                TimberWrapper.initConfig(appContext);
            }
            WorkManagerPruner.getInstance(appContext).pruneWorkManagerIfNeeded();
            return true;
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Context appContext = activity.getApplication();
        if (loggerWeaverInterface == null) {
            loggerWeaverInterface = new LoggerWeaverInterface(appContext);
        }
        Weaver.Companion.executeWeaveCoRoutineWithFirebase(loggerWeaverInterface, ENABLE_ASYNC_USER_BASED_TIMBER_SESSION, appContext);
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