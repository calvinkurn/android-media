package com.tokopedia.media.loader.internal

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import com.tokopedia.applink.RouteManager.getIntent
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform.MEDIA_QUALITY_SETTING
import com.tokopedia.dev_monitoring_tools.session.SessionDataUsageLogger
import com.tokopedia.device.info.DeviceConnectionInfo.getConnectionType
import com.tokopedia.logger.utils.Priority
import com.tokopedia.media.loader.R
import com.tokopedia.media.loader.data.HIGH_QUALITY
import com.tokopedia.unifycomponents.Toaster
import java.util.concurrent.TimeUnit
import com.tokopedia.media.loader.internal.NetworkManager.state as networkManagerState

internal class MediaLoaderActivityLifecycle(
    private val context: Context
) : ActivityLifecycleCallbacks {

    private val preferences by lazy { MediaSettingPreferences(context) }

    private val logger by lazy {
        SessionDataUsageLogger(
            priority = Priority.P2,
            sessionName = ACTIVE_SESSION_NAME,
            dataUsageName = DATA_USAGE_NAME,
            intervalSession = INTERVAL_SESSION
        )
    }

    override fun onActivityStarted(activity: Activity) {
        val className = activity.javaClass.canonicalName ?: return

        val isFromWhiteList = !WHITELIST.singleOrNull { it == className }.isNullOrEmpty()
        val toasterVisibility = !preferences.toasterVisibility()
        val isQualitySettingHigh = networkManagerState(context) != HIGH_QUALITY

        if (isFromWhiteList && toasterVisibility && isQualitySettingHigh) {
            try {
                showToaster(activity)
            } catch (ignored: Exception) {}
        }
    }

    override fun onActivityPaused(activity: Activity) {
        logger.returnFromOtherActivity = true
    }

    override fun onActivityDestroyed(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        logger.openedPageCount++
        logger.openedPageCountTotal++

        logger.addJourney(activity)
    }

    override fun onActivityResumed(activity: Activity) {
        if (logger.returnFromOtherActivity) logger.addJourney(activity)
        if (logger.running) return

        Thread {
            Runnable {
                logger.checkSession(
                    activityName = activity.javaClass.simpleName,
                    connectionType = getConnectionType(activity)
                )
            }
        }.start()
    }

    private fun showToaster(activity: Activity) {
        Handler(Looper.getMainLooper()).postDelayed({
            with(activity) {
                window.decorView.findViewById<ViewGroup>(android.R.id.content)?.let {
                    preferences.hasShowToaster(true) // set flag

                    // show toaster
                    Toaster.build(
                        view = it,
                        text = getString(R.string.media_toaster_title),
                        actionText = getString(R.string.media_toaster_cta),
                        duration = Toaster.LENGTH_LONG,
                        type = Toaster.TYPE_NORMAL,
                        clickListener = {
                            startActivity(getIntent(this, MEDIA_QUALITY_SETTING))
                        }
                    ).show()
                }
            }
        }, DELAY_PRE_SHOW_TOAST)
    }

    companion object {
        private const val DELAY_PRE_SHOW_TOAST = 2500L

        // for data usage logger
        private const val ACTIVE_SESSION_NAME = "MEDIALOADER_ACTIVE_SESSION"
        private const val DATA_USAGE_NAME = "MEDIALOADER_DATA_USAGE"

        private val INTERVAL_SESSION = TimeUnit.MINUTES.toMillis(1)

        private val WHITELIST = arrayOf(
            "com.tokopedia.product.detail.view.activity.ProductDetailActivity",
            "com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity",
            "com.tokopedia.search.result.presentation.view.activity.SearchActivity"
        )
    }

}
