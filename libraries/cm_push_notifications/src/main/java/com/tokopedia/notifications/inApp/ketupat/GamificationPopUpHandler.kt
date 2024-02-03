package com.tokopedia.notifications.inApp.ketupat

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.notifications.common.CMConstant
import com.tokopedia.notifications.domain.data.GamiScratchCardPreEvaluate
import com.tokopedia.notifications.domain.data.PopUpContent
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.date.DateUtil.getCurrentDate
import com.tokopedia.utils.date.getDayDiffFromToday
import java.lang.ref.WeakReference
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.absoluteValue

class GamificationPopUpHandler {

    private var isAnimationPopupGQlCalled = false

    fun onFragmentResume(activity: Activity) {
        try {
            val activityName = activity::class.java.simpleName
            val isAnimationPopupEnabled = FirebaseRemoteConfigImpl(activity.applicationContext)
                .getBoolean("ketupat_animation_popup_enable", true)
            if (activityName == HomePageActivity) {
                // get active campaigns list for in app
                // if list size > 0 show in app and return
                // else show popup
                if (!isAnimationPopEnable(activity)) {
                    return
                }
                if (isAnimationPopupEnabled) {
                    if (!isAnimationPopupGQlCalled) {
                        isAnimationPopupGQlCalled = true
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

    fun getScratchCardData(
        activity: Activity,
        pageSource: String = "tokopedia-home-page",
        ketupatSlashCallBack: KetupatSlashCallBack? = null
    ) {
        val userSession = createUserSession(activity)
        if (userSession.isLoggedIn) {
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

    private fun enableGQLCall() {
        Handler().postDelayed({
            isAnimationPopupGQlCalled = false
        }, 2000)
    }

    open fun showLottiePopup(
        activity: Activity,
        pageSource: String,
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
            if (pageSource == "tokopedia-home-page") {
                setTimeStampForKetupat(activity)
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

    private fun isAnimationPopEnable(activity: Activity): Boolean {
        val ketupatShownTime: String? = getShownTime(activity, "ketupat_shown_time")
        val inappShownTime: String? = getShownTime(activity, "inapp_shown_time")

        // IF inshown or ketupat dialog shown before 24 hours then simply return

        return ketupatShownTime?.let { is24HourBefore(it) } ?: true &&
            inappShownTime?.let { is24HourBefore(it) } ?: true
    }

    private fun getShownTime(context: Context, key: String): String? {
        return getSharedPref(context, key).getString(key, null)
    }

    private fun is24HourBefore(date: String): Boolean {
        return date.toDate("EEE MMM dd HH:mm:ss zzz yyyy").getDayDiffFromToday().absoluteValue.toInt() >= 1
    }

    private fun setTimeStampForKetupat(activity: Activity) {
        getSharedPref(activity, "ketupat_shown_time").edit().putString(
            "ketupat_shown_time",
            getCurrentDate().toString()
        ).apply()
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

    companion object {
        @JvmStatic
        fun String?.toDate(format: String): Date {
            this?.let {
                val fromFormat: DateFormat = SimpleDateFormat(format, Locale.ENGLISH)
                return fromFormat.parse(it)
                    ?: throw ParseException("Date doesn't valid ($this) with format $format", 0)
            }
            throw ParseException("Date doesn't valid ($this) with format $format", 0)
        }

        private const val HomePageActivity = "MainParentActivity"
    }
}
