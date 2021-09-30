package com.tokopedia.review.common.presentation.listener

import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey

interface ReviewBasicInfoListener {

    fun onUserNameClicked(userId: String)

    fun shouldShowCredibilityComponent(): Boolean {
        return try {
            RemoteConfigInstance.getInstance().abTestPlatform.getString(
                RollenceKey.EXPERIMENT_NAME_REVIEW_CREDIBILITY,
                RollenceKey.VARIANT_REVIEW_CREDIBILITY_WITHOUT_BOTTOM_SHEET
            ) == RollenceKey.VARIANT_REVIEW_CREDIBILITY_WITH_BOTTOM_SHEET
        } catch (t: Throwable) {
            false
        }
    }

    fun trackOnUserInfoClicked(feedbackId: String, userId: String, statistics: String)
}