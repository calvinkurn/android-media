package com.tokopedia.sellerfeedback

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.screenshot_observer.Screenshot
import com.tokopedia.sellerfeedback.SellerFeedbackConstants.REMOTE_CONFIG_ENABLE_SELLER_GLOBAL_FEEDBACK
import com.tokopedia.sellerfeedback.SellerFeedbackConstants.REMOTE_CONFIG_ENABLE_SELLER_GLOBAL_FEEDBACK_DEFAULT
import com.tokopedia.sellerfeedback.presentation.fragment.SellerFeedbackFragment


class SellerFeedbackScreenshot(private val context: Context) : Screenshot(context.contentResolver) {

    companion object {
        private const val THRESHOLD_TIME = 1000L
    }

    private var lastTimeCall = 0L
    private var lastTimeUpdate = 0L

    private var remoteConfig: FirebaseRemoteConfigImpl? = null
    private var currentActivity: Activity? = null

    override var listener = object : BottomSheetListener {
        override fun onFeedbackClicked(uri: Uri?, className: String, isFromScreenshot: Boolean) {
            uri?.let { openFeedbackForm(it) }
        }
    }

    override fun onScreenShotTaken(uri: Uri) {
        val enableSellerFeedbackScreenshot = getEnableSellerGlobalFeedbackRemoteConfig(currentActivity)
        if (enableSellerFeedbackScreenshot) {
            SellerFeedbackTracking.Impression.eventViewHomepage()

            lastTimeCall = System.currentTimeMillis()
            if (lastTimeCall - lastTimeUpdate > THRESHOLD_TIME) {
                super.onScreenShotTaken(uri)
                lastTimeUpdate = System.currentTimeMillis()
            }
        }
    }

    override fun onActivityResumed(activity: Activity) {
        currentActivity = activity
        super.onActivityResumed(activity)
    }

    private fun getEnableSellerGlobalFeedbackRemoteConfig(activity: Activity?): Boolean {
        return activity?.let {
            if (remoteConfig == null) {
                remoteConfig = FirebaseRemoteConfigImpl(activity)
            }
            remoteConfig?.getBoolean(
                    REMOTE_CONFIG_ENABLE_SELLER_GLOBAL_FEEDBACK,
                    REMOTE_CONFIG_ENABLE_SELLER_GLOBAL_FEEDBACK_DEFAULT
            ) ?: REMOTE_CONFIG_ENABLE_SELLER_GLOBAL_FEEDBACK_DEFAULT
        } ?: REMOTE_CONFIG_ENABLE_SELLER_GLOBAL_FEEDBACK_DEFAULT
    }

    private fun openFeedbackForm(uri: Uri) {
        val intent = RouteManager.getIntent(context, ApplinkConst.SellerApp.SELLER_FEEDBACK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(SellerFeedbackFragment.EXTRA_URI_IMAGE, uri)
        context.startActivity(intent)
    }
}