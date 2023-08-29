package com.tokopedia.trackingoptimizer.activitylifecyclecallback

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.tokopedia.trackingoptimizer.TrackingQueue

class TrackingQueueActivityLifecycleCallback(val context: Context) : Application.ActivityLifecycleCallbacks {

    private val trackingQueue: TrackingQueue by lazy {
        TrackingQueue(context)
    }

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        // no op
    }

    override fun onActivityStarted(activity: Activity) {
        // no op
    }

    override fun onActivityResumed(activity: Activity) {
        // no op
    }

    override fun onActivityPaused(activity: Activity) {
        // send all tracking when onPaused
        trackingQueue.sendAll()
    }

    override fun onActivityStopped(activity: Activity) {
        // no op
    }

    override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
        // no op
    }

    override fun onActivityDestroyed(activity: Activity) {
        // no op
    }
}
