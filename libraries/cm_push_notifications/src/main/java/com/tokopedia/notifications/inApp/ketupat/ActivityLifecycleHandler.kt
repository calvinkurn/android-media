package com.tokopedia.notifications.inApp.ketupat

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
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
import java.util.concurrent.TimeUnit

open class ActivityLifecycleHandler : Application.ActivityLifecycleCallbacks {

    private var isAnimationPopupGQlCalled = false

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        isAnimationPopupGQlCalled = false
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
                    if(!isAnimationPopupGQlCalled) {
                        getScratchCardData(activity)
                    }
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

    open fun getScratchCardData(
        activity: Activity,
        pageSource: String = "tokopedia-home-page",
        ketupatSlashCallBack: KetupatSlashCallBack? = null
    ) {
        val userSession = createUserSession(activity)
        if (userSession.isLoggedIn) {
            isAnimationPopupGQlCalled = true
            AnimationPopupGqlGetData().getAnimationScratchPopupData({
                it.popUpContent?.let { popup ->
                    if (popup.isShown == true) {
                        showLottiePopup(
                            activity,
                            pageSource,
                            getSlugData(it),
                            popup,
                            getScratchCardIdData(it),
                            ketupatSlashCallBack
                        )
                    }
                }
                enableGQLCall()
            }, {
                enableGQLCall()
            }, pageSource)
        }
    }

    private fun enableGQLCall(){
        Handler().postDelayed({
            isAnimationPopupGQlCalled = false
                              }, 5000)
    }

    open fun showLottiePopup(
        activity: Activity,
        pageSource:String,
        slug: String?,
        popUpContent: PopUpContent,
        scratchCardId: String,
        ketupatSlashCallBack: KetupatSlashCallBack? = null
    ) {
        try {
            if (pageSource == "tokopedia-home-page" && !isAnimationPopEnable(activity)) {
                return
            }
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
            if(pageSource == "tokopedia-home-page")
                setTimeStampForKetupat(activity)
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

    private fun isAnimationPopEnable(activity: Activity): Boolean {
        val ketupatShownTime: Long = getShownTime(activity, "ketupat_shown_time")
        val inappShownTime: Long = getShownTime(activity, "inapp_shown_time")

        // IF inshown or ketupat dialog shown before 24 hours then simply return

        return (is24HourBefore(ketupatShownTime) && is24HourBefore(inappShownTime))
    }

    private fun getShownTime(context: Context, key: String): Long {
        return getSharedPref(context, key).getLong(key, 0)
    }

    private fun is24HourBefore(time: Long): Boolean {
        return TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - time) >= 5
    }

    private fun setTimeStampForKetupat(activity: Activity) {
        getSharedPref(activity, "ketupat_shown_time").edit().putLong("ketupat_shown_time", System.currentTimeMillis()).apply()
    }

    private fun getSharedPref(context: Context, key: String) = context.applicationContext.getSharedPreferences(
        key,
        Context.MODE_PRIVATE
    )
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

    override fun onActivityStopped(activity: Activity) {
//        isAnimationPopupGQlCalled = false
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) { }

    override fun onActivityDestroyed(activity: Activity) { }

    companion object {
        private const val HomePageActivity = "MainParentActivity"
    }
}
