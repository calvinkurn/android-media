package com.tokopedia.notifications.inApp.ketupat

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.domain.data.GamiScratchCardPreEvaluate
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import java.lang.ref.WeakReference

open class ActivityLifecycleHandler: Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        val activityName = activity::class.java.simpleName
        if (activityName == HomePageActivity) {
            getScratchCardData(activity)
        }
    }

    open fun getScratchCardData(activity: Activity) {
        val userSession = createUserSession(activity)
        AnimationPopupGqlGetData().getAnimationScratchPopupData({
                showLottiePopup(activity, getSlugData(it))
        },{})
    }

    open fun showLottiePopup(activity: Activity, slug: String?) {
        try {
            val currentActivity: WeakReference<Activity> =
                WeakReference(activity)
            val ketupatAnimationPopup = KetupatAnimationPopup(activity.applicationContext, null, activity)
            val weakActivity = currentActivity.get() ?: return
            ketupatAnimationPopup.loadLottieAnimation(slug)
            val root = weakActivity.window
                .decorView
                .findViewById<View>(android.R.id.content)
                .rootView as FrameLayout
            root.addView(ketupatAnimationPopup)
        } catch (e : Exception) {
            ServerLogger.log(
                Priority.P2, "CM_VALIDATION",
                mapOf("type" to "exception",
                    "err" to Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT),
                    "data" to ""
                ))
        }
    }

    private fun getSlugData(gamiScratchCardData: GamiScratchCardPreEvaluate?): String? {
        return gamiScratchCardData?.scratchCard?.slug
    }
    private fun createUserSession(activity: Activity): UserSessionInterface =
        UserSession(activity)

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
