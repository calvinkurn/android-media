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

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        //no op
    }

    override fun onActivityStarted(p0: Activity) {
        //no op
    }

    override fun onActivityResumed(p0: Activity) {
        //no op
    }

    override fun onActivityPaused(p0: Activity) {
        trackingQueue.sendAll()
    }

    override fun onActivityStopped(p0: Activity) {
        //no op
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        //no op
    }

    override fun onActivityDestroyed(p0: Activity) {
        //no op
    }

}
