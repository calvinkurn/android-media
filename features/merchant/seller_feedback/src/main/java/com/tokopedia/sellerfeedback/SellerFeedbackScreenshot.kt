package com.tokopedia.sellerfeedback

import android.app.Activity
import android.content.ContentResolver
import android.net.Uri
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.screenshot_observer.Screenshot
import com.tokopedia.sellerfeedback.SellerFeedbackConstants.REMOTE_CONFIG_ENABLE_SELLER_GLOBAL_FEEDBACK
import com.tokopedia.sellerfeedback.SellerFeedbackConstants.REMOTE_CONFIG_ENABLE_SELLER_GLOBAL_FEEDBACK_DEFAULT

class SellerFeedbackScreenshot(
        contentResolver: ContentResolver,
        bottomSheetListener: BottomSheetListener
) : Screenshot(contentResolver, bottomSheetListener) {

    private var remoteConfig: FirebaseRemoteConfigImpl? = null
    private var currentActivity: Activity? = null

    override fun onScreenShotTaken(uri: Uri) {
        val enableSellerFeedbackScreenshot = getEnableSellerGlobalFeedbackRemoteConfig(currentActivity)
        if (enableSellerFeedbackScreenshot) {
            SellerFeedbackTracking.Impression.eventViewHomepage()
            super.onScreenShotTaken(uri)
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

}