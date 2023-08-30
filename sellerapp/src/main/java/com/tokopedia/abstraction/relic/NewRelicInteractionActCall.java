package com.tokopedia.abstraction.relic;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.newrelic.agent.android.NewRelic;
import com.tokopedia.user.session.UserSessionInterface;

import timber.log.Timber;

public class NewRelicInteractionActCall implements Application.ActivityLifecycleCallbacks {

    public NewRelicInteractionActCall(UserSessionInterface userSession) {
        this.userSession = userSession;
    }

    private static final String ATTRIBUTE_ACTIVITY = "activityName";

    private final UserSessionInterface userSession;

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
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
        NewRelic.setAttribute(ATTRIBUTE_ACTIVITY, activity.getLocalClassName());
        Timber.tag("milhamj").e("Class simple name: " + activity.getClass().getSimpleName());
        Timber.tag("milhamj").e("Class name: " + activity.getClass().getName());
        Timber.tag("milhamj").e("Local class name: " + activity.getLocalClassName());
        Timber.tag("milhamj").e("Component class name: " + activity.getComponentName().getClassName());
        Timber.tag("milhamj").e("Component short class name: " + activity.getComponentName().getShortClassName());
        String eMessage = "Class simple name: " + activity.getClass().getSimpleName()
                + "Class name: " + activity.getClass().getName()
                + "Local class name: " + activity.getLocalClassName()
                + "Component class name: " + activity.getComponentName().getClassName()
                + "Component short class name: " + activity.getComponentName().getShortClassName();
        IllegalStateException exception = new IllegalStateException(eMessage);
        exception.printStackTrace();
        try {
            FirebaseCrashlytics.getInstance().recordException(exception);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
