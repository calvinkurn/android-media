package com.tokopedia.people.analytic

import com.tokopedia.people.views.uimodel.UserReviewUiModel
import javax.inject.Inject

/**
 * Created By : Jonathan Darwin on June 08, 2023
 */
class UserReviewImpressCoordinator @Inject constructor() {

    private val mDataImpress = mutableListOf<UserReviewUiModel.Review>()

    fun impress(
        dataImpress: UserReviewUiModel.Review,
        callback: (dataImpress: UserReviewUiModel.Review) -> Unit,
    ) {
        val findReview = mDataImpress.filter { it.feedbackID == dataImpress.feedbackID }
        if (!findReview.isNullOrEmpty()) return

        callback.invoke(dataImpress)
        mDataImpress.add(dataImpress)
    }

    fun sendTracker(callback: () -> Unit) {
        callback.invoke()
        if (!mDataImpress.isNullOrEmpty()) mDataImpress.clear()
    }
}
