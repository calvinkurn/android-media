package com.tokopedia.review.feature.createreputation.presentation.listener

interface ReviewBadRatingCategoryListener {
    fun onBadRatingCategoryClicked(title: String, isSelectedBoolean: Boolean, badRatingCategoryId: Int)
}