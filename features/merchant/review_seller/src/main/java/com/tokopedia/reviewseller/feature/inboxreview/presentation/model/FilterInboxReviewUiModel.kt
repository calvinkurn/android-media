package com.tokopedia.reviewseller.feature.inboxreview.presentation.model

import com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter.BaseInboxReview
import com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter.InboxReviewAdapterTypeFactory

data class FilterInboxReviewUiModel(
        var sortFilterInboxItemList: ArrayList<SortFilterInboxItemWrapper> = arrayListOf()
): BaseInboxReview {
    override fun type(typeFactory: InboxReviewAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}