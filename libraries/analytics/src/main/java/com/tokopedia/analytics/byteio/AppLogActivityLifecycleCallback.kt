package com.tokopedia.analytics.byteio

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.analytics.byteio.AppLogAnalytics.createdInOnCreate
import com.tokopedia.analytics.byteio.AppLogAnalytics.removePageName
import com.tokopedia.analytics.byteio.AppLogAnalytics.sendStayProductDetail
import java.lang.ref.WeakReference

class AppLogActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        setCurrent(activity, true)
        if (isPdpPage(activity) && activity is BaseSimpleActivity) {
            // In case activity is first running, we put the startTime.
            // in onResume this will not be overridden
            activity.startTime = System.currentTimeMillis()
        }
    }

    override fun onActivityStarted(activity: Activity) {
        setCurrent(activity, false)
    }

    private fun setCurrent(activity: Activity, fromOnCreate: Boolean) {
        if (activity == AppLogAnalytics.currentActivityReference?.get()) {
            return
        }
        createdInOnCreate = fromOnCreate
        AppLogAnalytics.currentActivityReference = WeakReference<Activity>(activity)
        AppLogAnalytics.currentActivityName = activity.javaClass.simpleName
        AppLogAnalytics.addPageName(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        if (isPdpPage(activity) && activity is BaseSimpleActivity) {
            // in case the activity is resuming, we start the startTime in onResume, not onCreate
            if (activity.startTime <= 0) {
                activity.startTime = System.currentTimeMillis()
            }
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (isPdpPage(activity) && activity is BaseSimpleActivity) {
            sendStayProductDetail(
                System.currentTimeMillis() - activity.startTime,
                (activity as IAppLogPdpActivity).getProductTrack(),
                getQuitType(activity)
            )
        }
    }

    private fun getQuitType(activity: Activity): String {
        //TODO need to revisit this logic
        return if (activity.isFinishing) {
            "return"
        } else if (createdInOnCreate) {
            "next"
        } else {
            "close"
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
