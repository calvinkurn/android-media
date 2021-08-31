package com.tokopedia.media.common.common

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.tokopedia.applink.RouteManager.getIntent
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.MEDIA_QUALITY_SETTING
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dev_monitoring_tools.session.SessionDataUsageLogger
import com.tokopedia.device.info.DeviceConnectionInfo.getConnectionType
import com.tokopedia.kotlin.extensions.view.formattedToMB
import com.tokopedia.logger.utils.Priority
import com.tokopedia.media.common.R
import com.tokopedia.media.common.data.HIGH_QUALITY
import com.tokopedia.media.common.data.MediaBitmapSize
import com.tokopedia.media.common.data.MediaSettingPreferences
import com.tokopedia.media.common.util.getDirSize
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import com.bumptech.glide.Glide.getPhotoCacheDir as getGlidePhotoCacheDir
import com.tokopedia.media.common.util.NetworkManager.state as networkManagerState

class MediaLoaderActivityLifecycle(
        private val context: Context
) : ActivityLifecycleCallbacks {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val preferences by lazy { MediaSettingPreferences(context) }
    private val bitmapSize by lazy { MediaBitmapSize(context) }

    private val logger by lazy {
        SessionDataUsageLogger(
            priority = Priority.P2,
            sessionName = ACTIVE_SESSION_NAME,
            dataUsageName = DATA_USAGE_NAME,
            intervalSession = INTERVAL_SESSION
        )
    }

    override fun onActivityStarted(activity: Activity) {
        if (!WHITELIST.singleOrNull {
                    it == activity.javaClass.canonicalName
                }.isNullOrEmpty()
                && !preferences.toasterVisibility()
                && networkManagerState(context) != HIGH_QUALITY) {
            try {
                showToaster(activity)
            } catch (ignored: Exception) {}
        }
    }

    override fun onActivityPaused(activity: Activity) {
        logger.returnFromOtherActivity = true
    }

    override fun onActivityDestroyed(activity: Activity) {
        scope.cancel()
    }

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        logger.openedPageCount++
        logger.openedPageCountTotal++
        logger.addJourney(activity)

        glideMigration()
    }

    override fun onActivityResumed(activity: Activity) {
        if (logger.returnFromOtherActivity) logger.addJourney(activity)
        if (logger.running) return

        val mediaAdditionalData = mapOf(
            KEY_ACCUMULATIVE_SIZE to bitmapSize.getSize().formattedToMB(),
            KEY_INTERNAL_CACHE_SIZE to activity.cacheDir.getDirSize().formattedToMB(),
            KEY_GLIDE_CACHE_SIZE to getGlidePhotoCacheDir(context).getDirSize().formattedToMB()
        )

        Thread {
            logger.addLogItems(mediaAdditionalData)

            logger.checkSession(
                activityName = activity.javaClass.simpleName,
                connectionType = getConnectionType(activity)
            )
        }.start()
    }

    private fun showToaster(activity: Activity) {
        Handler().postDelayed({
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

    private fun glideMigration() {
        if (!preferences.glideMigration() && GlobalConfig.VERSION_NAME == MIGRATION_VERSION_SUPPORT) {
            scope.launch {
                Glide.get(context).clearDiskCache()

                withContext(Dispatchers.Main) {
                    preferences.hasGlideMigration(true)
                }
            }
        }
    }

    companion object {
        private const val MIGRATION_VERSION_SUPPORT = "3.138"
        private const val DELAY_PRE_SHOW_TOAST = 2500L

        // for data usage logger
        private const val ACTIVE_SESSION_NAME = "MEDIALOADER_ACTIVE_SESSION"
        private const val DATA_USAGE_NAME = "MEDIALOADER_DATA_USAGE"

        // key for an additional data usage logger
        private const val KEY_ACCUMULATIVE_SIZE = "accumulative_size"
        private const val KEY_INTERNAL_CACHE_SIZE = "internal_cachedir_size"
        private const val KEY_GLIDE_CACHE_SIZE = "glide_dir_size"

        private val INTERVAL_SESSION = TimeUnit.MINUTES.toMillis(1)

        private val WHITELIST = arrayOf(
                "com.tokopedia.product.detail.view.activity.ProductDetailActivity",
                "com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity",
                "com.tokopedia.search.result.presentation.view.activity.SearchActivity"
        )
    }

}