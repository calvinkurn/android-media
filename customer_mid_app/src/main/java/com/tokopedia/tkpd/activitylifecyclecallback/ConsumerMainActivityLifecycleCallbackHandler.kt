package com.tokopedia.tkpd.activitylifecyclecallback

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

class ConsumerMainActivityLifecycleCallbackHandler(private val application: Application) :
    Application.ActivityLifecycleCallbacks {

    @Inject
    lateinit var trackingQueue: TrackingQueue

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {
        TODO("Not yet implemented")
    }

    override fun onActivityStarted(p0: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityResumed(p0: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityPaused(p0: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivityStopped(p0: Activity) {
        TODO("Not yet implemented")
    }

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
        TODO("Not yet implemented")
    }

    override fun onActivityDestroyed(p0: Activity) {
        TODO("Not yet implemented")
    }

}
