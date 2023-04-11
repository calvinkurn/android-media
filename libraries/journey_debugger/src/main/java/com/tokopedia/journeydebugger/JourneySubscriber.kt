package com.tokopedia.journeydebugger

import android.app.Activity
import android.app.Application
import android.os.Bundle

class JourneySubscriber() : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }

    override fun onActivityResumed(activity: Activity) {
        JourneyLogger.getInstance(activity).save(activity::class.java.name)
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
    }
}
