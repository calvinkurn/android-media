package com.tokopedia.media.common.common

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.MEDIA_QUALITY_SETTING
import com.tokopedia.media.common.R
import com.tokopedia.media.common.data.HIGH_QUALITY
import com.tokopedia.media.common.data.MediaSettingPreferences
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext
import com.tokopedia.media.common.util.NetworkManager.state as networkManagerState

class MediaLoaderActivityLifecycle(
        private val context: Context
) : ActivityLifecycleCallbacks, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.IO

    private val preferences by lazy { MediaSettingPreferences(context) }

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

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityDestroyed(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {
        if (!preferences.glideMigration()) {
            launch {
                Glide.get(context).clearDiskCache()

                withContext(Dispatchers.Main) {
                    preferences.setGlideMigration(true)
                }
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    private fun showToaster(activity: Activity) {
        Handler().postDelayed({
            with(activity) {
                window.decorView.findViewById<ViewGroup>(android.R.id.content)?.let {
                    // set flag
                    preferences.setToasterVisibilityFlag(true)

                    // show toaster
                    Toaster.build(
                            view = it,
                            text = getString(R.string.media_toaster_title),
                            actionText = getString(R.string.media_toaster_cta),
                            duration = Toaster.LENGTH_LONG,
                            type = Toaster.TYPE_NORMAL,
                            clickListener = View.OnClickListener {
                                startActivity(RouteManager.getIntent(this, MEDIA_QUALITY_SETTING))
                            }
                    ).show()
                }
            }
        }, DELAY_PRE_SHOW_TOAST)
    }

    companion object {
        private const val DELAY_PRE_SHOW_TOAST = 2500L

        private const val TIME_FORMAT = "%.2f"
        private val INTERVAL_SESSION = TimeUnit.MINUTES.toMillis(1)
        private const val SIZE_FORMAT = "%.2f"
        private const val MB_SIZE: Long = 1000000

        private val WHITELIST = arrayOf(
                "com.tokopedia.product.detail.view.activity.ProductDetailActivity",
                "com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity",
                "com.tokopedia.search.result.presentation.view.activity.SearchActivity"
        )
    }

}