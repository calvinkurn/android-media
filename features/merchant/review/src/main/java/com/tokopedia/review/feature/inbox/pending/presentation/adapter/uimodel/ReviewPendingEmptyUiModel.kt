package com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.inbox.pending.data.ReviewPendingEmptyState
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.ReviewPendingAdapterTypeFactory

class ReviewPendingEmptyUiModel(
        val emptyState: ReviewPendingEmptyState
) : Visitable<ReviewPendingAdapterTypeFactory> {

    override fun type(typeFactory: ReviewPendingAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}