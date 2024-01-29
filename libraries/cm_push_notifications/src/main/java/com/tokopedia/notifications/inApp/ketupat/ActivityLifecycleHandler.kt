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
import com.tokopedia.notifications.domain.data.PopUpContent
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import java.lang.ref.WeakReference

open class ActivityLifecycleHandler : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
//        try {
//            val activityName = activity::class.java.simpleName
//            val isAnimationPopupEnabled = FirebaseRemoteConfigImpl(activity.applicationContext)
//                .getBoolean("ketupat_animation_popup_enable", true)
//            if (activityName == HomePageActivity) {
//                //get active campaigns list for in app
//                //if list size > 0 show in app and return
//                //else show popup
//                if (!CMInAppManager.getInstance().existsActiveInAppCampaign(HomePageActivity, true)
//                    && isAnimationPopupEnabled
//                ) {
//                    getScratchCardData(activity)
//                }
//            }
//        } catch (e: Exception) {
//            ServerLogger.log(
//                Priority.P2,
//                "KETUPAT_ANIMATION_POPUP",
//                mapOf(
//                    "type" to "exception",
//                    "err" to Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT),
//                    "data" to ""
//                )
//            )
//        }
    }

    override fun onActivityResumed(activity: Activity) {
        try {
            val activityName = activity::class.java.simpleName
            val isAnimationPopupEnabled = FirebaseRemoteConfigImpl(activity.applicationContext)
                .getBoolean("ketupat_animation_popup_enable", true)
            if (activityName == HomePageActivity) {
                // get active campaigns list for in app
                // if list size > 0 show in app and return
                // else show popup
                if (!CMInAppManager.getInstance().existsActiveInAppCampaign(HomePageActivity, true) &&
                    isAnimationPopupEnabled
                ) {
                    getScratchCardData(activity)
                }
            }
        } catch (e: Exception) {
            ServerLogger.log(
                Priority.P2,
                "KETUPAT_ANIMATION_POPUP",
                mapOf(
                    "type" to "exception",
                    "err" to Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT),
                    "data" to ""
                )
            )
        }
    }

    open fun getScratchCardData(activity: Activity,
                                pageSource: String = "tokopedia-home-page",
                                ketupatSlashCallBack: KetupatSlashCallBack? = null) {
        val userSession = createUserSession(activity)
        if (userSession.isLoggedIn) {
            AnimationPopupGqlGetData().getAnimationScratchPopupData({
                it.popUpContent?.let { popup ->
                    if(popup.isShown == true) {
                        showLottiePopup(activity, getSlugData(it), popup,
                            getScratchCardIdData(it), ketupatSlashCallBack)
                    }
                }
            }, {}, pageSource)
        }
    }

    open fun showLottiePopup(
        activity: Activity,
        slug: String?,
        popUpContent: PopUpContent,
        scratchCardId: String,
        ketupatSlashCallBack: KetupatSlashCallBack? = null
    ) {
        try {
            val currentActivity: WeakReference<Activity> =
                WeakReference(activity)
            val ketupatAnimationPopup = KetupatAnimationPopup(activity.applicationContext, null, activity)
            val weakActivity = currentActivity.get() ?: return
            ketupatAnimationPopup.loadLottieAnimation(slug, popUpContent, scratchCardId, ketupatSlashCallBack)
            val root = weakActivity.window
                .decorView
                .findViewById<View>(android.R.id.content)
                .rootView as FrameLayout
            root.addView(ketupatAnimationPopup)
        } catch (e: Exception) {
            ServerLogger.log(
                Priority.P2,
                "KETUPAT_ANIMATION_POPUP",
                mapOf(
                    "type" to "exception",
                    "err" to Log.getStackTraceString(e).take(CMConstant.TimberTags.MAX_LIMIT),
                    "data" to ""
                )
            )
        }
    }

    private fun getSlugData(gamiScratchCardData: GamiScratchCardPreEvaluate?): String {
        return gamiScratchCardData?.scratchCard?.slug ?: ""
    }

    private fun getScratchCardIdData(gamiScratchCardData: GamiScratchCardPreEvaluate?): String {
        return gamiScratchCardData?.scratchCard?.id.toString()
    }
    private fun createUserSession(activity: Activity): UserSessionInterface =
        UserSession(activity)

    override fun onActivityStarted(activity: Activity) { }

    override fun onActivityPaused(activity: Activity) { }

    override fun onActivityStopped(activity: Activity) { }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) { }

    override fun onActivityDestroyed(activity: Activity) { }

    companion object {
        private const val HomePageActivity = "MainParentActivity"
    }
}
