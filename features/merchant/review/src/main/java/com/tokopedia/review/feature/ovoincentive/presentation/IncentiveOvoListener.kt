package com.tokopedia.review.feature.ovoincentive.presentation

interface IncentiveOvoListener {
    fun onUrlClicked(url: String): Boolean
    fun onClickCloseThankYouBottomSheet()
    fun onClickReviewAnother()
}