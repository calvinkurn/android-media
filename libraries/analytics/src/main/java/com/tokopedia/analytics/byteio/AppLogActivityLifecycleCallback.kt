package com.tokopedia.analytics.byteio

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.analytics.byteio.AppLogAnalytics.popPageData
import com.tokopedia.analytics.byteio.AppLogAnalytics.pushPageData
import com.tokopedia.analytics.byteio.AppLogAnalytics.removePageName
import com.tokopedia.analytics.byteio.pdp.AppLogPdp.sendStayProductDetail
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

class AppLogActivityLifecycleCallback : Application.ActivityLifecycleCallbacks, CoroutineScope {

    private var pdpCheckpointStay: WeakReference<Activity>? = null

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        AppLogAnalytics.activityCount++
        if (isPdpPage(activity) && activity is BaseSimpleActivity) {
            // In case activity is first running, we put the startTime.
            // in onResume this will not be overridden
            activity.startTime = System.currentTimeMillis()
        }
        if (activity is AppLogInterface) {
            AppLogAnalytics.pushPageData()
            AppLogAnalytics.putPageData(AppLogParam.PAGE_NAME, activity.getPageName())
            if (activity.isEnterFromWhitelisted()) {
                AppLogAnalytics.putPageData(AppLogParam.ENTER_FROM, activity.getPageName())
            }
        }
    }

    override fun onActivityStarted(activity: Activity) {
        setCurrent(activity)
    }

    private fun setCurrent(activity: Activity) {
        AppLogAnalytics.currentActivityReference = WeakReference<Activity>(activity)
        AppLogAnalytics.currentActivityName = activity.javaClass.simpleName
        AppLogAnalytics.addPageName(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        if (isPdpPage(activity) && activity is BaseSimpleActivity) {
            // in case the activity is resuming, we start the startTime in onResume, not onCreate
            if (activity.startTime == 0L) {
                activity.startTime = System.currentTimeMillis()
            }
        }
        if (isAtcVariantPage(activity) && activity is BaseSimpleActivity) {
            val pdpActivity = pdpCheckpointStay?.get()
            if (pdpActivity is IAppLogPdpActivity && pdpActivity is BaseSimpleActivity && pdpActivity.startTime == 0L) {
                pdpActivity.startTime = System.currentTimeMillis()
            }
        }
    }

    override fun onActivityPaused(activity: Activity) {
        if (isPdpPage(activity) && activity is BaseSimpleActivity) {
            launch {
                if (activity is IAppLogPdpActivity && activity.isExiting().not()) {
                    pdpCheckpointStay = WeakReference(activity)
                    return@launch
                }
                suspendSendStayProductDetail(
                    System.currentTimeMillis() - activity.startTime,
                    (activity as IAppLogPdpActivity).getProductTrack(),
                    activity.isFinishing, AppLogAnalytics.activityCount
                )
                activity.startTime = 0L
            }
        }
        // Sending stay data in ATC Variant when it's a close, not sending when it's a return
        if (isAtcVariantPage(activity) && activity is BaseSimpleActivity && !activity.isFinishing) {
            launch {
                val pdpActivity = pdpCheckpointStay?.get()
                if (pdpActivity is IAppLogPdpActivity && pdpActivity is BaseSimpleActivity) {
                    suspendSendStayProductDetail(
                        System.currentTimeMillis() - pdpActivity.startTime,
                        (pdpActivity as IAppLogPdpActivity).getProductTrack(),
                        pdpActivity.isFinishing, AppLogAnalytics.activityCount
                    )
                    pdpActivity.startTime = 0L
                }
            }
        }
    }

    private suspend fun suspendSendStayProductDetail(
        durationInMs: Long,
        product: TrackStayProductDetail,
        isFinishing: Boolean,
        prevActCount: Int
    ) {
        if (isFinishing) {
            sendStayProductDetail(
                durationInMs,
                product, QuitType.RETURN
            )
            return
        }
        delay(500)
        val quitType = if (AppLogAnalytics.activityCount > prevActCount) {
            QuitType.NEXT
        } else {
            QuitType.CLOSE
        }
        sendStayProductDetail(
            durationInMs,
            product, quitType
        )
    }

    override fun onActivityStopped(activity: Activity) {
        // noop
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        // noop
    }

    override fun onActivityDestroyed(activity: Activity) {
        AppLogAnalytics.activityCount--
        if (getCurrentActivity() === activity) {
            AppLogAnalytics.currentActivityReference?.clear()
        }
        removePageName(activity)
        if (activity is AppLogInterface) {
            AppLogAnalytics.popPageData()
        }
    }

    private fun getCurrentActivity(): Activity? {
        return AppLogAnalytics.currentActivityReference?.get()
    }

    private fun isPdpPage(activity: Activity): Boolean {
        return (activity is IAppLogPdpActivity &&
            activity.getPageName() == PageName.PDP)
    }

    private fun isAtcVariantPage(activity: Activity): Boolean {
        return (activity is IAppLogActivity &&
            activity.getPageName() == PageName.SKU)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }
}
