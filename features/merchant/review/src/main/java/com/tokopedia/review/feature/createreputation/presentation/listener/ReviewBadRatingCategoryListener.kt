package com.tokopedia.review.feature.createreputation.presentation.listener

interface ReviewBadRatingCategoryListener {
    fun onBadRatingCategoryClicked(title: String, isSelected: Boolean, badRatingCategoryId: String, shouldRequestFocus: Boolean)
    fun onImpressBadRatingCategory(title: String)
}