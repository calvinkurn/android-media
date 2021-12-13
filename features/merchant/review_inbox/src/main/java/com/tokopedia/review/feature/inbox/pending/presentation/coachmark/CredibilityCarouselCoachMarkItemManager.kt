package com.tokopedia.review.feature.inbox.pending.presentation.coachmark

import android.view.View
import com.tokopedia.review.feature.inbox.pending.common.constants.ReviewPendingCoachMarkData
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.CoachMarkUiModel
import com.tokopedia.review.inbox.R

class CredibilityCarouselCoachMarkItemManager(
    override var viewHolderRootView: View? = null,
    override var uiModel: CoachMarkUiModel? = null,
    override var key: String = ReviewPendingCoachMarkData.CREDIBILITY_CAROUSEL_COACH_MARK_KEY,
    override var title: Int = R.string.review_pending_credibility_coach_mark_title,
    override var description: Int = R.string.review_pending_credibility_coach_mark_subtitle
) : CoachMarkItemManager() {
    override fun getAnchorView(): View? {
        return viewHolderRootView
    }
}