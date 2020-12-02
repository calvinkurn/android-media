package com.tokopedia.promotionstarget.presentation.subscriber

import android.app.Activity
import android.app.Application
import android.os.Bundle

interface BaseApplicationLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
    override fun onActivityResumed(activity: Activity?) {
    }

    override fun onActivityPaused(activity: Activity?) {
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityDestroyed(activity: Activity) {
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
    }

    override fun onActivityStopped(activity: Activity) {
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
    }
}