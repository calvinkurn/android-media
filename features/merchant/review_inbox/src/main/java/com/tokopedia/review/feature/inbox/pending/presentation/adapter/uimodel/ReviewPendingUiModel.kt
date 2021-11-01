package com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.review.feature.inbox.pending.data.ProductrevWaitForFeedback
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.ReviewPendingAdapterTypeFactory

class ReviewPendingUiModel(
        val productrevWaitForFeedback: ProductrevWaitForFeedback
) : Visitable<ReviewPendingAdapterTypeFactory> {

    override fun type(typeFactory: ReviewPendingAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}