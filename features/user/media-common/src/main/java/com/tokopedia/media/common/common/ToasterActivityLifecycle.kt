package com.tokopedia.media.common.common

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import android.widget.Toast

class ToasterActivityLifecycle: ActivityLifecycleCallbacks {

    override fun onActivityStarted(activity: Activity) {
        val isCurrentPage = WHITELIST.singleOrNull {
            it == activity.javaClass.canonicalName
        }

        if (!isCurrentPage.isNullOrEmpty()) {
            Toast.makeText(
                    activity.applicationContext,
                    "Kualitas gambarmu diturunkan karena jaringan HP-mu lebih rendah dari 4G.",
                    Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityDestroyed(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, bundle: Bundle) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivityCreated(activity: Activity, bundle: Bundle?) {}

    override fun onActivityResumed(activity: Activity) {}

    companion object {
        private val WHITELIST = arrayOf(
                "com.tokopedia.product.detail.view.activity.ProductDetailActivity",
                "com.tokopedia.navigation.presentation.activity.MainParentActivity",
                "com.tokopedia.shop.pageheader.presentation.activity.ShopPageActivity",
                "com.tokopedia.search.result.presentation.view.activity.SearchActivity"
        )
    }
    
}