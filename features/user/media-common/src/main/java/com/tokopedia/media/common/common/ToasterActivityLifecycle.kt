package com.tokopedia.media.common.common

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewGroup
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal.IMAGE_QUALITY_SETTING
import com.tokopedia.media.common.R
import com.tokopedia.unifycomponents.Toaster
import java.lang.Exception

class ToasterActivityLifecycle : ActivityLifecycleCallbacks {

    override fun onActivityStarted(activity: Activity) {
        if (!WHITELIST.singleOrNull {
            it == activity.javaClass.canonicalName
        }.isNullOrEmpty()) {
            try {
                showToaster(activity)
            } catch (ignored: Exception) {}
        }
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityDestroyed(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}

    override fun onActivityResumed(activity: Activity) {}

    private fun showToaster(activity: Activity) {
        Handler().postDelayed({
            with(activity) {
                window.decorView.findViewById<ViewGroup>(android.R.id.content)?.let {
                    Toaster.make(
                            view = it,
                            text = getString(R.string.media_toaster_title),
                            actionText = getString(R.string.media_toaster_cta),
                            duration = Toaster.LENGTH_LONG,
                            type = Toaster.TYPE_NORMAL,
                            clickListener = View.OnClickListener {
                                startActivity(RouteManager.getIntent(this, IMAGE_QUALITY_SETTING))
                            }
                    )
                }
            }
        }, 2000)
    }

    companion object {
        private val WHITELIST = arrayOf(
                "com.tokopedia.product.detail.view.activity.ProductDetailActivity",
                "com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity",
                "com.tokopedia.search.result.presentation.view.activity.SearchActivity"
        )
    }

}