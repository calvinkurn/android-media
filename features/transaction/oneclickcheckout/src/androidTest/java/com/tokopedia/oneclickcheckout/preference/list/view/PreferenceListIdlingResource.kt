package com.tokopedia.oneclickcheckout.preference.list.view

import android.app.Activity
import androidx.appcompat.app.AlertDialog
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.test.espresso.IdlingResource

class ProgressDialogIdlingResource(private var progressDialogGetter: () -> AlertDialog?) : IdlingResource {
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName(): String {
        return "Progress Dialog Idling Resource"
    }

    override fun isIdleNow(): Boolean {
        val progressDialog = progressDialogGetter.invoke()
        if (progressDialog?.isShowing == true) return false
        resourceCallback?.onTransitionToIdle()
        return true
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        resourceCallback = callback
    }

}

class SwipeRefreshIdlingResource(private val activity: Activity, private val swipeRefreshResourceId: Int) : IdlingResource {
    private var resourceCallback: IdlingResource.ResourceCallback? = null
    override fun getName(): String {
        return "Swipe Refresh Idling Resource"
    }

    override fun isIdleNow(): Boolean {
        val swipeRefresh: SwipeRefreshLayout = activity.findViewById(swipeRefreshResourceId)
                ?: return false
        if (swipeRefresh.isRefreshing) {
            return false
        }
        resourceCallback?.onTransitionToIdle()
        return true
    }

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        resourceCallback = callback
    }
}
