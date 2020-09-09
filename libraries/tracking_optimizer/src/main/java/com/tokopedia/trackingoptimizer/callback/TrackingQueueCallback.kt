package com.tokopedia.trackingoptimizer.callback

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.tokopedia.trackingoptimizer.TrackingQueue

class TrackingQueueCallback : Application.ActivityLifecycleCallbacks {
    lateinit var trackingQueue: TrackingQueue

    override fun onActivityPaused(p0: Activity) {
        getTrackingQueueObject(p0).sendAll()
    }

    override fun onActivityStarted(p0: Activity) {
    }

    override fun onActivityDestroyed(p0: Activity) {
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
    }

    override fun onActivityStopped(p0: Activity) {
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        trackingQueue = TrackingQueue(p0.applicationContext)
    }

    override fun onActivityResumed(p0: Activity) {
    }

    private fun getTrackingQueueObject(activity: Activity) : TrackingQueue {
        if (!::trackingQueue.isInitialized || trackingQueue == null) {
            return TrackingQueue(activity.applicationContext)
        }
        return trackingQueue
    }
}