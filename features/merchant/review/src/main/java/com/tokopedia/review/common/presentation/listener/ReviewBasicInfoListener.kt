package com.tokopedia.review.common.presentation.listener

import android.content.Context
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
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
}