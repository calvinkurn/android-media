package com.tokopedia.analytics.byteio

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.analytics.byteio.AppLogAnalytics.removePageName
import com.tokopedia.analytics.byteio.AppLogAnalytics.sendStay
import java.lang.ref.WeakReference

class AppLogActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (isPdpPage(activity) && activity is BaseSimpleActivity) {
            activity.startTime = System.currentTimeMillis()
        }
    }

    override fun onActivityStarted(activity: Activity) {
        if (activity == AppLogAnalytics.currentActivityReference?.get()) {
            return
        }
        AppLogAnalytics.currentActivityReference = WeakReference<Activity>(activity)
        AppLogAnalytics.currentActivityName = activity.javaClass.simpleName
        AppLogAnalytics.addPageName(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        if (isPdpPage(activity) && activity is BaseSimpleActivity) {
            if (activity.startTime <= 0) {
                activity.startTime = System.currentTimeMillis()
            }
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (isPdpPage(activity) && activity is BaseSimpleActivity) {
            sendStay(
                System.currentTimeMillis() - activity.startTime,
                (activity as IAppLogPdpActivity).getProductTrack()
            )
        }
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
            removePageName(activity)
        }
    }

    private fun getCurrentActivity(): Activity? {
        return AppLogAnalytics.currentActivityReference?.get()
    }

    private fun isPdpPage(activity: Activity): Boolean {
        return (activity is IAppLogPdpActivity &&
                activity.getPageName() == PageName.PDP)
    }
}
