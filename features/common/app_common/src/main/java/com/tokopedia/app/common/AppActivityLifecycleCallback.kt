package com.tokopedia.app.common

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.tokopedia.applink.AppUtil
import java.lang.ref.WeakReference

class AppActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        //noop
    }

    override fun onActivityStarted(activity: Activity) {
        // technically this should be in onResume but it is effectively the same to have it here, plus
        // it allows us to use currentActivityReference_ in session initialization code
        AppUtil.currentActivityReference = WeakReference<Activity>(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        //noop
    }

    override fun onActivityPaused(activity: Activity) {
        //noop
    }

    override fun onActivityStopped(activity: Activity) {
        //noop
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        //noop
    }

    override fun onActivityDestroyed(activity: Activity) {
        if (getCurrentActivity() === activity) {
            AppUtil.currentActivityReference?.clear()
        }
    }

    fun getCurrentActivity(): Activity? {
        return AppUtil.currentActivityReference?.get()
    }

}