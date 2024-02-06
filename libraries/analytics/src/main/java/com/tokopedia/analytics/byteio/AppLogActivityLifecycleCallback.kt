package com.tokopedia.analytics.byteio

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.tokopedia.analytics.byteio.AppLogAnalytics.startTime
import java.lang.ref.WeakReference

class AppLogActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        startTime = System.currentTimeMillis()
    }

    override fun onActivityStarted(activity: Activity) {
        if (activity == AppLogAnalytics.currentActivityReference?.get()) {
            return
        }
        AppLogAnalytics.currentActivityReference = WeakReference<Activity>(activity)
        if (activity is IAppLogActivity) {
            AppLogAnalytics.pageNames.add(activity.getPageName())
        } else {
            AppLogAnalytics.pageNames.add(null)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        if (startTime == 0L) {
            startTime = System.currentTimeMillis()
        }
    }

    override fun onActivityPaused(activity: Activity) {
        // noop
    }

    override fun onActivityStopped(activity: Activity) {
        // noop
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // noop
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (getCurrentActivity() === activity) {
            AppLogAnalytics.currentActivityReference?.clear()
            AppLogAnalytics.pageNames.removeLast()
        }
    }

    private fun getCurrentActivity(): Activity? {
        return AppLogAnalytics.currentActivityReference?.get()
    }
}
