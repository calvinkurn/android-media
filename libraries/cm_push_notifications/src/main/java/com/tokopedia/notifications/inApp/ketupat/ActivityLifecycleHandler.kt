package com.tokopedia.notifications.inApp.ketupat

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.R
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.inApp.viewEngine.ViewEngine
import java.lang.ref.WeakReference

open class ActivityLifecycleHandler: Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val activityName = activity::class.java.simpleName
//        showLottiePopup(activity)
//        if (activityName == HomePageActivity) {
//            showLottiePopup(activity)
//        }
    }

    open fun showLottiePopup(activity: Activity) {
        try {
            val currentActivity: WeakReference<Activity> =
                WeakReference(activity)
            val view = LayoutInflater.from(currentActivity.get())
                .inflate(R.layout.layout_inapp_animation, null, false)
            val ketupatAnimationPopup = KetupatAnimationPopup(activity.applicationContext, null, activity)
//            val mainContainer = view.findViewById<View>(R.id.mainContainer)
            val activity = currentActivity.get() ?: return
//            val viewEngine = ViewEngine(activity)
            ketupatAnimationPopup.loadLottieAnimation()
//            val view = viewEngine.createView(cmInApp) ?: return
            val inAppViewPrev = activity.findViewById<View>(R.id.mainContainer)
            if (null != inAppViewPrev) return
            val root = activity.window
                .decorView
                .findViewById<View>(android.R.id.content)
                .rootView as FrameLayout
            root.addView(ketupatAnimationPopup)
//            cmDialogHandlerCallback.onShow(activity)
        } catch (e : Exception) {
            ServerLogger.log(
                Priority.P2, "CM_VALIDATION",
                mapOf("type" to "exception",
                    "err" to Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT),
                    "data" to ""
                ))
//            cmDialogHandlerCallback.onException(e, cmInApp)
        }
    }

    override fun onActivityStarted(activity: Activity) { }

    override fun onActivityResumed(activity: Activity) { }

    override fun onActivityPaused(activity: Activity) { }

    override fun onActivityStopped(activity: Activity) { }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) { }

    override fun onActivityDestroyed(activity: Activity) { }

    companion object {
        private const val HomePageActivity = "MainParentActivity"
    }
}
