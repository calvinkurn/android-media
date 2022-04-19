package com.tokopedia.review.feature.inboxreview.presentation.model

import com.tokopedia.review.feature.inboxreview.presentation.adapter.BaseInboxReview
import com.tokopedia.review.feature.inboxreview.presentation.adapter.InboxReviewAdapterTypeFactory

data class InboxReviewEmptyUiModel(var isFilter: Boolean): BaseInboxReview {
    override fun type(typeFactory: InboxReviewAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}