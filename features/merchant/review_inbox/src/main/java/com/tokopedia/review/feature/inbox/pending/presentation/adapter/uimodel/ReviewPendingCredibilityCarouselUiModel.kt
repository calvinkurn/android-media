package com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.ReviewPendingTypeFactory
import com.tokopedia.review.feature.inbox.pending.presentation.coachmark.CredibilityCarouselCoachMarkItemManager
import com.tokopedia.review.feature.inbox.pending.presentation.coachmark.ReviewPendingCoachMarkItemManager

data class ReviewPendingCredibilityCarouselUiModel(
    val items: List<ReviewPendingCredibilityUiModel>
) : Visitable<ReviewPendingTypeFactory>, CoachMarkUiModel {

    override fun type(typeFactory: ReviewPendingTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun getCoachMarkItemManager(): ReviewPendingCoachMarkItemManager? {
        return CredibilityCarouselCoachMarkItemManager(uiModel = this)
    }
}