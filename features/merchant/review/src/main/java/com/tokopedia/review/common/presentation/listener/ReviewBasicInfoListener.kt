package com.tokopedia.review.common.presentation.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.review.feature.reading.presentation.fragment.ReadReviewFragment

interface ReviewBasicInfoListener {

    fun onUserNameClicked(context: Context, userId: String) {
        RouteManager.route(
            context,
            ApplinkConstInternalMarketplace.REVIEW_CREDIBILITY,
            userId,
            ReadReviewFragment.READING_SOURCE
        )
    }

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
}