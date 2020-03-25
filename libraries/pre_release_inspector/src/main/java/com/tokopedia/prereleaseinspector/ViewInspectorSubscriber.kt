package com.tokopedia.prereleaseinspector

import android.app.Activity
import android.app.Application
import android.os.Bundle

class ViewInspectorSubscriber() : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
    }

    override fun onActivityResumed(activity: Activity) {
        ViewInspectorManager.register(activity)
    }

    override fun onActivityPaused(activity: Activity) {
        ViewInspectorManager.unregister()
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