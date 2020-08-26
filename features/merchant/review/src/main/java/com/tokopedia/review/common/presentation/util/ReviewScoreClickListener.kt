package com.tokopedia.review.common.presentation.util

interface ReviewScoreClickListener {
    fun onReviewScoreClicked(score: Int): Boolean
}