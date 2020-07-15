package com.tokopedia.review.feature.historydetails.data

sealed class ReviewDetailScoreState {
    data class ReviewDetailScoreEditable(val currentScore: ReviewDetailScore): ReviewDetailScoreState()
    data class ReviewDetailScoreFinal(val currentScore: ReviewDetailScore): ReviewDetailScoreState()
}

sealed class ReviewDetailScore {
    object ReviewDetailScoreBad: ReviewDetailScore()
    object ReviewDetailScoreMediocre: ReviewDetailScore()
    object ReviewDetailScoreExcellent: ReviewDetailScore()
}