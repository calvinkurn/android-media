package com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter

import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.FeedbackInboxUiModel

interface FeedbackInboxReviewListener {
    fun onItemReplyOrEditClicked(data: FeedbackInboxUiModel, isReply: Boolean, adapterPosition: Int)
    fun onImageItemClicked(titleProduct: String, imageUrls: List<String>, thumbnailsUrl: List<String>, feedbackId: String, position: Int)
}

interface GlobalErrorStateListener {
    fun onActionGlobalErrorStateClicked()
}

interface RatingListListener {
    fun onItemRatingClicked(selected: Boolean, adapterPosition: Int)
}