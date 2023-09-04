package com.tokopedia.people.analytic

import com.tokopedia.people.di.UserProfileScope
import com.tokopedia.people.views.uimodel.UserReviewUiModel
import com.tokopedia.trackingoptimizer.TrackingQueue
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on June 08, 2023
 */
@UserProfileScope
class UserReviewImpressCoordinator @Inject constructor(
    private val trackingQueue: TrackingQueue,
) {
    private val mDataImpress = mutableListOf<UserReviewUiModel.Review>()

    fun impress(
        dataImpress: UserReviewUiModel.Review,
        callback: (dataImpress: UserReviewUiModel.Review) -> Unit,
    ) {
        val isReviewFound = mDataImpress.any { it.feedbackID == dataImpress.feedbackID }
        if (isReviewFound) return

        callback.invoke(dataImpress)
        mDataImpress.add(dataImpress)
    }

    fun sendTracker() {
        trackingQueue.sendAll()
        if (mDataImpress.isNotEmpty()) mDataImpress.clear()
    }
}
