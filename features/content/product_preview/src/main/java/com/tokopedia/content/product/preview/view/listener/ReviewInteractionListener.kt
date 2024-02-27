package com.tokopedia.content.product.preview.view.listener

import com.tokopedia.content.product.preview.view.uimodel.review.ReviewAuthorUiModel

interface ReviewInteractionListener {
    fun onReviewMediaScrolled()
    fun onPauseResumeVideo()
    fun onReviewCredibilityClicked(author: ReviewAuthorUiModel)
    fun onMenuClicked()
    fun onLike(isDoubleTap: Boolean)
    fun updateReviewWatchMode()
}
