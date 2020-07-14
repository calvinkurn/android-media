package com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter

import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.FeedbackInboxUiModel
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.FilterInboxReviewUiModel

interface FeedbackInboxReviewListener {
    fun onItemReplyOrEditClicked(data: FeedbackInboxUiModel, isReply: Boolean, adapterPosition: Int)
    fun onImageItemClicked(imageUrls: List<String>, thumbnailsUrl: List<String>, feedbackId: String, position: Int)
}

interface FilterInboxReviewListener {
    fun onItemFilterRatingClicked(data: FilterInboxReviewUiModel, adapterPosition: Int)
    fun onItemFilterStatusClicked(data: FilterInboxReviewUiModel, adapterPosition: Int)
}

interface GlobalErrorStateListener {
    fun onActionGlobalErrorStateClicked()
}