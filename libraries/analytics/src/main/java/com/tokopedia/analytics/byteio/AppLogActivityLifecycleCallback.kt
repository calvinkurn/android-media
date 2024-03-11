package com.tokopedia.analytics.byteio

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.analytics.byteio.AppLogAnalytics.currentActivityName
import com.tokopedia.analytics.byteio.AppLogAnalytics.removePageData
import com.tokopedia.analytics.byteio.AppLogAnalytics.pushPageData
import com.tokopedia.analytics.byteio.AppLogAnalytics.removeGlobalParam
import com.tokopedia.analytics.byteio.AppLogAnalytics.removePageName
import com.tokopedia.analytics.byteio.pdp.AppLogPdp.sendStayProductDetail
import com.tokopedia.kotlin.extensions.view.orZero
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import kotlin.coroutines.CoroutineContext

class AppLogActivityLifecycleCallback : Application.ActivityLifecycleCallbacks, CoroutineScope {

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        AppLogAnalytics.activityCount++
        if (isPdpPage(activity) && activity is BaseSimpleActivity) {
            // In case activity is first running, we put the startTime.
            // in onResume this will not be overridden
            activity.startTime = System.currentTimeMillis()
        }
        if (activity is AppLogInterface) {
            pushPageData(activity)
        }
        setCurrent(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        // no op
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
        removeGlobalParam()
    }

    override fun onActivityPaused(activity: Activity) {
        // no op
    }

    private suspend fun suspendSendStayProductDetail(
        durationInMs: Long,
        product: TrackStayProductDetail,
        isFinishing: Boolean,
        hash: Int
    ) {
        if (isFinishing) {
            sendStayProductDetail(
                durationInMs,
                product, QuitType.RETURN
            )
            return
        }
        delay(300)
        val quitType = if (AppLogAnalytics.getPreviousHash() == hash
            && currentActivityName != "AtcVariantActivity") {
            QuitType.NEXT
        } else {
            QuitType.CLOSE
        }
        sendStayProductDetail(
            durationInMs, product, quitType
        )
    }

    override fun onActivityStopped(activity: Activity) {
        if (isPdpPage(activity) && activity is BaseSimpleActivity) {
            launch {
                val hash = (activity as? AppLogInterface)?.hashCode().orZero()
                suspendSendStayProductDetail(
                    System.currentTimeMillis() - activity.startTime,
                    (activity as IAppLogPdpActivity).getProductTrack(),
                    activity.isFinishing, hash
                )
                activity.startTime = 0L
            }
        }
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
            removePageData(activity)
        }
    }

    private fun getCurrentActivity(): Activity? {
        return AppLogAnalytics.currentActivityReference?.get()
    }

    private fun isPdpPage(activity: Activity): Boolean {
        return (activity is IAppLogPdpActivity &&
            activity.getPageName() == PageName.PDP)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO + CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
        }
}
