package com.tokopedia.devicefingerprint.datavisor.lifecyclecallback

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisorWorker

class DataVisorLifecycleCallbacks: Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        DataVisorWorker.scheduleWorker(activity)
    }

    override fun onActivityStarted(activity: Activity) { }

    override fun onActivityResumed(activity: Activity) { }

    override fun onActivityPaused(activity: Activity) { }

    override fun onActivityStopped(activity: Activity) { }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) { }

    override fun onActivityDestroyed(activity: Activity) { }
}