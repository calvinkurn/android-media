package com.tokopedia.nps.helper

import android.app.Activity
import android.util.Log
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigKey
import timber.log.Timber

object InAppReviewHelper {
    const val CACHE_IN_APP_REVIEW = "CACHE_IN_APP_REVIEW"
    const val CACHE_KEY_HAS_SHOWN_BEFORE = "CACHE_KEY_HAS_SHOWN_BEFORE"

    @JvmOverloads
    @JvmStatic
    fun launchInAppReview(activity: Activity, callback: Callback? = null): Boolean {
        try {
            val remoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(activity)

            val hasShownInAppReviewBefore: Boolean = getInAppReviewHasShownBefore(activity)
            val enableInAppReview: Boolean = remoteConfig.getBoolean(RemoteConfigKey.ENABLE_IN_APP_REVIEW_DIGITAL_THANKYOU_PAGE, false)

            if (enableInAppReview && !hasShownInAppReviewBefore) {
                val inAppReviewManager: ReviewManager = ReviewManagerFactory.create(activity)
                inAppReviewManager.requestReviewFlow().addOnCompleteListener { request ->
                    if (request.isSuccessful) {
                        ServerLogger.log(Priority.P1, "IN_APP_REVIEW_STAT", mapOf("type" to "request", "activity" to activity.javaClass.name))
                        inAppReviewManager.launchReviewFlow(activity, request.result).addOnCompleteListener {
                            ServerLogger.log(Priority.P1, "IN_APP_REVIEW_STAT", mapOf("type" to "shown", "activity" to activity.javaClass.name))
                            setInAppReviewHasShownBefore(activity)
                            callback?.onCompleted()
                        }
                    } else {
                        callback?.onCompleted()
                    }
                }
                return true
            } else {
                return false
            }
        } catch (e: Exception) {
            ServerLogger.log(Priority.P1, "IN_APP_REVIEW_STAT", mapOf("type" to "error", "activity" to activity.javaClass.name, "err" to Log.getStackTraceString(e)))
            return false
        }
    }

    private fun getInAppReviewHasShownBefore(activity: Activity): Boolean {
        val cacheHandler = LocalCacheHandler(activity, CACHE_IN_APP_REVIEW)
        return cacheHandler.getBoolean(CACHE_KEY_HAS_SHOWN_BEFORE)
    }

    private fun setInAppReviewHasShownBefore(activity: Activity) {
        val cacheHandler = LocalCacheHandler(activity, CACHE_IN_APP_REVIEW)
        cacheHandler.putBoolean(CACHE_KEY_HAS_SHOWN_BEFORE, true)
        cacheHandler.applyEditor()
    }

    interface Callback {
        fun onCompleted()
    }
}