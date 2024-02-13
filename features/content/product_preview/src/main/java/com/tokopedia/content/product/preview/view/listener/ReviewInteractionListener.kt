package com.tokopedia.content.product.preview.view.listener

import com.tokopedia.content.product.preview.view.uimodel.review.ReviewAuthorUiModel
import com.tokopedia.content.product.preview.view.uimodel.review.ReviewLikeUiState

interface ReviewInteractionListener {
    fun onPauseResumeVideo()
    fun onReviewCredibilityClicked(author: ReviewAuthorUiModel)
    fun onMenuClicked()
    fun onLike(status: ReviewLikeUiState)
    fun updateReviewWatchMode()
}
