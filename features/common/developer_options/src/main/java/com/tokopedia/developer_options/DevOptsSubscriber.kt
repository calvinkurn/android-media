package com.tokopedia.developer_options

import android.app.Activity
import android.app.Application
import android.os.Bundle

class DevOptsSubscriber() : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
    }

    override fun onActivityResumed(activity: Activity) {
        DevOptsManager.register(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        DevOptsManager.unregister()
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