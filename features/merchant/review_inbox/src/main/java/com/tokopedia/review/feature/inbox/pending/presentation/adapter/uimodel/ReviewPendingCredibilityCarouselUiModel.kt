package com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.ReviewPendingTypeFactory

data class ReviewPendingCredibilityCarouselUiModel(
    val items: List<ReviewPendingCredibilityUiModel>
) : Visitable<ReviewPendingTypeFactory> {

    override fun type(typeFactory: ReviewPendingTypeFactory): Int {
        return typeFactory.type(this)
    }
}