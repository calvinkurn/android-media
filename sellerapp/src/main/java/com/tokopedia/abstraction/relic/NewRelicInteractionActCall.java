package com.tokopedia.abstraction.relic;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.newrelic.agent.android.NewRelic;
import com.tokopedia.user.session.UserSessionInterface;
import com.tokopedia.weaver.WeaveInterface;
import com.tokopedia.weaver.Weaver;

import org.jetbrains.annotations.NotNull;

public class NewRelicInteractionActCall implements Application.ActivityLifecycleCallbacks {

    public NewRelicInteractionActCall(UserSessionInterface userSession) {
        this.userSession = userSession;
    }

    //should be replaced with : android_enable_new_relic_async_trace
    private static final String ENABLE_ASYNC_NEW_RELIC_TRACE = "android_seller_app_persona_compose_enabled";
    private static final String ATTRIBUTE_ACTIVITY = "activityName";

    private final UserSessionInterface userSession;

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        startNewRelicOnBackground(activity);
    }

    private void startNewRelicOnBackground(Activity activity) {
        WeaveInterface weave = new WeaveInterface() {
            @NotNull
            @Override
            public Boolean execute() {
                try {
                    startNewRelicTracing(activity);
                } catch (Throwable ignored) {
                }
                return true;
            }
        };
        Weaver.Companion.executeWeaveCoRoutineNow(weave);
    }

    private void startNewRelicTracing(Activity activity) {
        if (userSession != null) {
            NewRelic.setUserId(userSession.getUserId());
        } else {
            NewRelic.setUserId("");
        }
        NewRelic.startInteraction(activity.getLocalClassName());
        NewRelic.setInteractionName(activity.getLocalClassName());
        setNewRelicAttribute(activity);
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
        setNewRelicAttribute(activity);
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    private void setNewRelicAttribute(Activity activity) {
        NewRelic.setAttribute(ATTRIBUTE_ACTIVITY, activity.getClass().getSimpleName());
    }
}
