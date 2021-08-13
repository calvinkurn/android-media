package com.tokopedia.abstraction.relic;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.newrelic.agent.android.NewRelic;

public class NewRelicInteractionActCall implements Application.ActivityLifecycleCallbacks {

    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        NewRelic.startInteraction(activity.getLocalClassName());
        NewRelic.setInteractionName(activity.getLocalClassName());
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    public void onActivityDestroyed(Activity activity) {
    }
}
