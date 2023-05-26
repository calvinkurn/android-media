package com.tokopedia.review.feature.media.detail.analytic

import com.tokopedia.reviewcommon.constant.AnalyticConstant
import com.tokopedia.reviewcommon.extension.appendBusinessUnit
import com.tokopedia.reviewcommon.extension.appendCurrentSite
import com.tokopedia.reviewcommon.extension.appendGeneralEventData
import com.tokopedia.reviewcommon.extension.appendProductId
import com.tokopedia.reviewcommon.extension.appendShopId
import com.tokopedia.reviewcommon.extension.appendTrackerIdIfNotBlank
import com.tokopedia.reviewcommon.extension.appendUserId
import com.tokopedia.reviewcommon.extension.sendGeneralEvent
import com.tokopedia.reviewcommon.feature.media.gallery.detailed.util.ReviewMediaGalleryRouter

object ReviewDetailTrackerHelper {

    var tracker: ReviewDetailTracker? = null
        private set

    fun setTracker(
        @ReviewMediaGalleryRouter.PageSource pageSource: Int,
    ) {
        if (pageSource == ReviewMediaGalleryRouter.PageSource.USER_PROFILE) {
            ReviewDetailUserProfileTracker()
        } else {
            ReviewDetailPdpTracker()
        }
    }
}
