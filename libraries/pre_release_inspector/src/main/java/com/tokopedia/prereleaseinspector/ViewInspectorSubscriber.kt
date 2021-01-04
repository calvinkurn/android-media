package com.tokopedia.prereleaseinspector

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseActivity

class ViewInspectorSubscriber() : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
    }

    override fun onActivityResumed(activity: Activity) {
        if (activity is BaseActivity) {
            activity.addListener(ViewInspectorVolumeListener(activity))
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (activity is BaseActivity) {
            activity.removeDebugVolumeListener()
        }
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