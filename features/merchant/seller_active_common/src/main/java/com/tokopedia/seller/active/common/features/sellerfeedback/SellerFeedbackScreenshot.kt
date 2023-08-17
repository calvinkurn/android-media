package com.tokopedia.seller.active.common.features.sellerfeedback

import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.sellerfeedback.DeeplinkMapperSellerFeedback
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.screenshot_observer.Screenshot
import com.tokopedia.seller.active.common.R
import com.tokopedia.unifycomponents.Toaster
import java.lang.ref.WeakReference

/**
 * Created by @ilhamsuaib on 21/03/23.
 */

class SellerFeedbackScreenshot(private val context: Context) : Screenshot(context.contentResolver) {

    companion object {
        private const val EXTRA_URI_IMAGE = "uri_image"
        private const val EXTRA_ACTIVITY_NAME = "extra_activity_name"
        private const val ACTIVITY_STATUS_PAUSED = 0
        private const val ACTIVITY_STATUS_RESUMED = 1
        private const val TOASTER_MARGIN_BOTTOM = 104
    }

    private var currentActivity: WeakReference<Activity>? = null
    private var onResumeCounter = ACTIVITY_STATUS_PAUSED
    private var isScreenShootToasterShown = false

    private val screenshotPreferenceManager by lazy { ScreenshotPreferenceManager(context) }

    override val toasterSellerListener = object : ToasterSellerListener {
        override fun showToaster(uri: Uri?, currentActivity: Activity?) {
            showToasterSellerFeedback(uri, currentActivity)
        }
    }

    override fun onScreenShotTaken(uri: Uri) {
        if (screenshotPreferenceManager.isScreenShootTriggerEnabled()) {
            processScreenshotTaken(uri)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = WeakReference(activity)
        super.onActivityResumed(activity)
    }

    private fun processScreenshotTaken(uri: Uri) {
        triggerScreenShotTaken(uri)
    }

    private fun triggerScreenShotTaken(uri: Uri) {
        SellerFeedbackTracking.eventViewHomepage()
        super.onScreenShotTaken(uri)
    }

    private fun openFeedbackForm(uri: Uri, currentActivity: Activity) {
        val intent = RouteManager.getIntent(context, DeeplinkMapperSellerFeedback.getSellerFeedbackInternalAppLink(currentActivity))
        val activityName = getActivityName(currentActivity)
        intent.putExtra(EXTRA_URI_IMAGE, uri)
        intent.putExtra(
            EXTRA_ACTIVITY_NAME,
            activityName
        )
        currentActivity.startActivity(intent)
        handleFeedbackSuccessToaster(currentActivity)
    }

    private fun getActivityName(currentActivity: Activity): String {
        return if (currentActivity.intent.data?.query.orEmpty().isNotEmpty()) {
            if (currentActivity::class.java.canonicalName.contains("webview")) {
                currentActivity::class.java.canonicalName + "?${
                    currentActivity.intent.data?.query.orEmpty()
                }"
            } else {
                currentActivity::class.java.canonicalName
            }
        } else {
            currentActivity::class.java.canonicalName
        }
    }

    private fun handleFeedbackSuccessToaster(currentActivity: Activity) {
        if (!screenshotPreferenceManager.isFeedbackFormSavedSuccess()) return

        (currentActivity as? ComponentActivity)?.lifecycle?.let { lifecycle ->
            lifecycle.addObserver(getLifecycleObserver(lifecycle, onResumeCallback = {
                currentActivity.window.decorView.rootView?.let { view ->
                    val isScreenShootTriggerEnabled =
                        screenshotPreferenceManager.isScreenShootTriggerEnabled()
                    SuccessToasterHelper.showToaster(
                        currentActivity,
                        view,
                        isScreenShootTriggerEnabled
                    )
                }
            }))
        }
    }

    private fun getLifecycleObserver(
        lifecycle: Lifecycle,
        onResumeCallback: () -> Unit
    ): LifecycleObserver {
        return object : DefaultLifecycleObserver {

            override fun onResume(owner: LifecycleOwner) {
                if (onResumeCounter == ACTIVITY_STATUS_RESUMED) {
                    onResumeCallback()
                    onResumeCounter = ACTIVITY_STATUS_PAUSED
                    lifecycle.removeObserver(this)
                } else {
                    onResumeCounter++
                }
            }
        }
    }

    private fun showToasterSellerFeedback(uri: Uri?, currentActivity: Activity?) {
        if (isScreenShootToasterShown) return
        isScreenShootToasterShown = true

        val view = currentActivity?.window?.decorView?.rootView
        view?.run {
            Toaster.toasterCustomBottomHeight = context.dpToPx(TOASTER_MARGIN_BOTTOM).toInt()
            Toaster.build(this,
                text = currentActivity.getString(R.string.screenshot_seller_feedback_toaster_text),
                actionText = currentActivity.getString(R.string.screenshot_seller_feedback_toaster_cta_text),
                type = Toaster.TYPE_NORMAL,
                duration = Toaster.LENGTH_LONG,
                clickListener = {
                    uri?.let { uri ->
                        SellerFeedbackTracking.eventClickFeedbackButton()
                        openFeedbackForm(uri, currentActivity)
                    }
                }
            ).addCallback(object : Snackbar.Callback() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    super.onDismissed(transientBottomBar, event)
                    isScreenShootToasterShown = false
                }
            }).show()
        }
    }
}
